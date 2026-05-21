#!/usr/bin/env python3
"""
paw_transform.py — Set the paw group's local position and rotation in PawsModel.java.

The group pivot is placed AT the paw centre in arm space, so these values
rotate/move the paw around its own centre, not the arm pivot point.

Usage:
    python3 paw_transform.py [options]

Position options (pixels, default 0):
    --x X   --y Y   --z Z

Rotation options (degrees, default from current file):
    --rx RX   --ry RY   --rz RZ

The script patches both paws_group and paws_slim_group in PawsModel.java.

Examples:
    python3 paw_transform.py --rz -90           # rotate 90° CW around arm axis
    python3 paw_transform.py --rx 45 --y 2      # tilt + nudge up 2px
    python3 paw_transform.py --x 0 --y 10 --z 0 --rx 0 --ry 0 --rz -90
"""

import re, sys, math, os

MODEL_PATH = os.path.join(os.path.dirname(__file__),
    "common/src/main/java/net/melatowoin/client/model/PawsModel.java")

# ── parse current values from file ────────────────────────────────────────────

def read_current(src):
    m = re.search(
        r'PartPose\.offsetAndRotation\(\s*'
        r'([\d.+-]+),\s*([\d.+-]+),\s*([\d.+-]+),\s*'   # x y z
        r'([^,]+),\s*([^,]+),\s*([^)]+)\)',              # rx ry rz
        src)
    if not m:
        return 0, 0, 0, 0, 0, 0   # defaults
    x, y, z = float(m.group(1)), float(m.group(2)), float(m.group(3))
    # rx/ry/rz may be expressions like -(float)(Math.PI/2) — evaluate safely
    def parse_angle(s):
        s = s.strip()
        s = s.replace('(float)', '').replace('Math.PI', str(math.pi))
        try:
            return math.degrees(float(eval(s)))
        except Exception:
            return 0.0
    rx = parse_angle(m.group(4))
    ry = parse_angle(m.group(5))
    rz = parse_angle(m.group(6))
    return x, y, z, rx, ry, rz

# ── arg parsing ───────────────────────────────────────────────────────────────

def get_arg(args, flag, default):
    try:
        i = args.index(flag)
        return float(args[i + 1])
    except (ValueError, IndexError):
        return default

args = sys.argv[1:]
if '-h' in args or '--help' in args:
    print(__doc__); sys.exit(0)

src = open(MODEL_PATH).read()
cx, cy, cz, crx, cry, crz = read_current(src)

x  = get_arg(args, '--x',  cx)
y  = get_arg(args, '--y',  cy)
z  = get_arg(args, '--z',  cz)
rx = get_arg(args, '--rx', crx)
ry = get_arg(args, '--ry', cry)
rz = get_arg(args, '--rz', crz)

# ── format ────────────────────────────────────────────────────────────────────

def fmt_f(v):
    """Format a float: strip trailing zeros, keep at most 4 decimal places."""
    s = f'{v:.4f}'.rstrip('0').rstrip('.')
    return s if s not in ('', '-') else '0'

def fmt_rad(deg):
    """Express degrees as a readable Java float in radians."""
    if deg == 0:
        return '0'
    r = math.radians(deg)
    # Express as N*(float)(Math.PI/X) when exact, else raw float
    for numer in range(1, 9):
        for denom in [1, 2, 3, 4, 6, 8, 12]:
            if abs(r - math.pi * numer / denom) < 1e-9:
                frac = f'Math.PI / {denom}' if denom > 1 else 'Math.PI'
                coef = f'{numer} * ' if numer > 1 else ''
                return f'(float)({coef}{frac})'
            if abs(r + math.pi * numer / denom) < 1e-9:
                frac = f'Math.PI / {denom}' if denom > 1 else 'Math.PI'
                coef = f'{numer} * ' if numer > 1 else ''
                return f'-(float)({coef}{frac})'
    return f'{r:.6f}f'

replacement = (f'PartPose.offsetAndRotation({fmt_f(x)}, {fmt_f(y)}, {fmt_f(z)}, '
               f'{fmt_rad(rx)}, {fmt_rad(ry)}, {fmt_rad(rz)})')

# Replace all occurrences — handle nested parens by counting depth
def replace_all(src, replacement):
    tag = 'PartPose.offsetAndRotation('
    result, n, i = [], 0, 0
    while i < len(src):
        idx = src.find(tag, i)
        if idx == -1:
            result.append(src[i:]); break
        result.append(src[i:idx])
        # find matching close paren
        depth, j = 0, idx + len(tag) - 1
        while j < len(src):
            if src[j] == '(': depth += 1
            elif src[j] == ')':
                depth -= 1
                if depth == 0: break
            j += 1
        result.append(replacement)
        n += 1
        i = j + 1
    return ''.join(result), n

new_src, n = replace_all(src, replacement)

if n == 0:
    sys.exit('ERROR: no PartPose.offsetAndRotation found in ' + MODEL_PATH)

with open(MODEL_PATH, 'w') as f:
    f.write(new_src)

print(f'Updated {n} group(s) → {replacement}')
print(f'  position : x={fmt_f(x)} y={fmt_f(y)} z={fmt_f(z)}')
print(f'  rotation : rx={rx:.1f}° ry={ry:.1f}° rz={rz:.1f}°')
