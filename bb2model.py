#!/usr/bin/env python3
"""
bb2model.py — Merge one or more Blockbench Java exports into a mod Model class.

Usage:
    python3 bb2model.py FILE [FILE ...] [--class ClassName] [--namespace modid] [--layer-id layer_id] [--out path]

    FILE         One or more Blockbench-exported .java files.
                 Each file becomes a named group; a renderXxx() method is generated per file.
    --class      Output Java class name (default: first file's base name, PascalCase + "Model")
    --namespace  Mod namespace for LAYER_LOCATION (default: melatowoin)
    --layer-id   Layer registry name (default: class name in lowercase)
    --out        Output file path (default: <ClassName>.java next to first input file)

Examples:
    # Single file → one render method
    python3 bb2model.py paws.java --class PawsModel

    # Two variants → renderPaws() and renderPawsSlim()
    python3 bb2model.py paws.java paws_slim.java --class PawsModel
"""

import re, sys, os, textwrap

# ── helpers ──────────────────────────────────────────────────────────────────

def close_paren(text, start):
    """Return index of ')' matching '(' at text[start]."""
    depth, i = 0, start
    while i < len(text):
        if text[i] == '(':   depth += 1
        elif text[i] == ')':
            depth -= 1
            if depth == 0: return i
        i += 1
    raise ValueError("Unmatched '('")

def close_brace(text, start):
    """Return index of '}' matching '{' at text[start]."""
    depth, i = 0, start
    while i < len(text):
        if text[i] == '{':   depth += 1
        elif text[i] == '}':
            depth -= 1
            if depth == 0: return i
        i += 1
    raise ValueError("Unmatched '{'")

def to_pascal(snake):
    """paws_slim → PawsSlim"""
    return ''.join(w.capitalize() for w in snake.split('_'))

def to_camel(snake):
    """paws_slim → pawsSlim"""
    parts = snake.split('_')
    return parts[0].lower() + ''.join(w.capitalize() for w in parts[1:])

# ── parser ────────────────────────────────────────────────────────────────────

def parse(path):
    """Return a dict with everything we need from a single Blockbench Java export."""
    src = open(path).read()
    base = os.path.splitext(os.path.basename(path))[0]   # e.g. "paws_slim"

    # Texture size
    m = re.search(r'LayerDefinition\.create\(\w+,\s*(\d+),\s*(\d+)\)', src)
    tex_w, tex_h = (int(m.group(1)), int(m.group(2))) if m else (32, 32)

    # Part names from constructor
    part_names = re.findall(r'root\.getChild\("(\w+)"\)', src)

    # createBodyLayer body
    m = re.search(r'static LayerDefinition createBodyLayer\(\)\s*\{', src)
    if not m:
        raise SystemExit(f"ERROR: no createBodyLayer() in {path}")
    bopen = src.index('{', m.start())
    cbl_body = src[bopen + 1 : close_brace(src, bopen)]

    # Parts rendered in renderToBuffer
    m2 = re.search(r'void renderToBuffer\([^)]+\)\s*\{', src)
    rendered = []
    if m2:
        bopen2 = src.index('{', m2.start())
        rendered = re.findall(r'(\w+)\.render\(', src[bopen2 + 1 : close_brace(src, bopen2)])

    return dict(base=base, tex_w=tex_w, tex_h=tex_h,
                part_names=part_names, cbl_body=cbl_body, rendered=rendered)

# ── transformer ───────────────────────────────────────────────────────────────

def prefix_body(cbl_body, prefix, part_names):
    """
    Rewrite a createBodyLayer body so every part name is prefixed.
    Returns just the addOrReplaceChild statements (no MeshDef/PartDef header,
    no return statement) ready to be embedded in the combined method.
    """
    body = cbl_body

    # Strip boilerplate header lines
    body = re.sub(r'\s*MeshDefinition\s+\w+\s*=\s*new MeshDefinition\(\);\s*', '\n', body)
    body = re.sub(r'\s*PartDefinition\s+partdefinition\s*=\s*\w+\.getRoot\(\);\s*', '\n', body)

    # Strip the return line
    body = re.sub(r'\s*return LayerDefinition\.create\([^;]+;\s*', '', body)

    # Prefix string literals that are part names: "print" → "pfx_print"
    def sub_str(m):
        name = m.group(1)
        return f'"{prefix}_{name}"' if name in part_names else m.group(0)
    body = re.sub(r'"(\w+)"', sub_str, body)

    # Prefix PartDefinition variable declarations and their dot-access usage.
    # Sort by length descending to avoid partial replacements.
    for name in sorted(part_names, key=len, reverse=True):
        pfx = f'{prefix}_{name}'
        body = re.sub(r'\bPartDefinition\s+' + re.escape(name) + r'\b',
                      f'PartDefinition {pfx}', body)
        # dotted access: name.addOrReplaceChild  (child bones)
        body = re.sub(r'\b' + re.escape(name) + r'(?=\.addOrReplaceChild)',
                      pfx, body)

    # Rename the root variable (partdefinition → root) so it matches our combined method
    body = re.sub(r'\bpartdefinition\b', 'root', body)

    return body.strip()

# ── generator ─────────────────────────────────────────────────────────────────

HEADER = '''\
package net.melatowoin.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class {class_name} extends Model {{

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation("{namespace}", "{layer_id}"), "main");

'''

def generate(infos, class_name, namespace, layer_id):
    tex_w = max(i['tex_w'] for i in infos)
    tex_h = max(i['tex_h'] for i in infos)

    lines = []
    lines.append(HEADER.format(class_name=class_name, namespace=namespace, layer_id=layer_id))

    # Field declarations
    for info in infos:
        pfx = info['base']
        for pname in info['part_names']:
            lines.append(f'    private final ModelPart {pfx}_{pname};\n')
    lines.append('\n')

    # Constructor
    lines.append(f'    public {class_name}(ModelPart root) {{\n')
    lines.append('        super(RenderType::entityCutoutNoCull);\n')
    for info in infos:
        pfx = info['base']
        for pname in info['part_names']:
            lines.append(f'        this.{pfx}_{pname} = root.getChild("{pfx}_{pname}");\n')
    lines.append('    }\n\n')

    # createBodyLayer
    lines.append('    public static LayerDefinition createBodyLayer() {\n')
    lines.append('        MeshDefinition mesh = new MeshDefinition();\n')
    lines.append('        PartDefinition root = mesh.getRoot();\n\n')
    for info in infos:
        pfx = info['base']
        body = prefix_body(info['cbl_body'], pfx, info['part_names'])
        # Indent each line by 8 spaces
        indented = '\n'.join('        ' + l if l.strip() else '' for l in body.splitlines())
        lines.append(f'        // ── {pfx} ──\n')
        lines.append(indented + '\n\n')
    lines.append(f'        return LayerDefinition.create(mesh, {tex_w}, {tex_h});\n')
    lines.append('    }\n\n')

    # One render method per input file
    for info in infos:
        pfx = info['base']
        method = 'render' + to_pascal(pfx)
        lines.append(f'    public void {method}(PoseStack pose, VertexConsumer buf,\n')
        lines.append( '            int light, int overlay, float r, float g, float b, float a) {\n')
        # Use the parts that appeared in the original renderToBuffer, or all parts if none found
        render_parts = info['rendered'] if info['rendered'] else info['part_names']
        for pname in render_parts:
            lines.append(f'        {pfx}_{pname}.render(pose, buf, light, overlay, r, g, b, a);\n')
        lines.append('    }\n\n')

    # renderToBuffer delegates to first file's method (required override)
    first_method = 'render' + to_pascal(infos[0]['base'])
    lines.append('    @Override\n')
    lines.append('    public void renderToBuffer(PoseStack pose, VertexConsumer buf,\n')
    lines.append('            int light, int overlay, float r, float g, float b, float a) {\n')
    lines.append(f'        {first_method}(pose, buf, light, overlay, r, g, b, a);\n')
    lines.append('    }\n')
    lines.append('}\n')

    return ''.join(lines)

# ── main ──────────────────────────────────────────────────────────────────────

def main():
    args = sys.argv[1:]
    if not args or args[0] in ('-h', '--help'):
        print(__doc__)
        sys.exit(0)

    files, class_name, namespace, layer_id, out_path = [], None, 'melatowoin', None, None
    i = 0
    while i < len(args):
        a = args[i]
        if a == '--class':     class_name = args[i+1]; i += 2
        elif a == '--namespace': namespace  = args[i+1]; i += 2
        elif a == '--layer-id': layer_id   = args[i+1]; i += 2
        elif a == '--out':      out_path    = args[i+1]; i += 2
        else:                   files.append(a); i += 1

    if not files:
        sys.exit('ERROR: no input files given')

    infos = [parse(f) for f in files]

    if class_name is None:
        class_name = to_pascal(infos[0]['base']) + 'Model'
    if layer_id is None:
        layer_id = infos[0]['base']
    if out_path is None:
        out_path = os.path.join(os.path.dirname(os.path.abspath(files[0])), class_name + '.java')

    java = generate(infos, class_name, namespace, layer_id)

    with open(out_path, 'w') as f:
        f.write(java)
    print(f'Wrote {out_path}')

if __name__ == '__main__':
    main()
