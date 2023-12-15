import time
import math
from itertools import combinations
from collections import defaultdict


def main(inputfile, part):
    with open(inputfile) as f:
        lines = ''.join(f.readlines()).strip()
        patterns = [pattern.split('\n') for pattern in lines.split('\n\n')]

    part = part_one if part == 1 else part_two

    return sum(part(pattern) for pattern in patterns)


def part_one(pattern):
    total = find_value(pattern, multiplier=100)
    pattern = transpose(pattern)
    total += find_value(pattern, multiplier=1)
    return total


def part_two(pattern):
    total = 0
    one = fix_smudge(pattern, multiplier=100)
    if one:
        return one
    else:
        pattern = transpose(pattern)
        return fix_smudge(pattern, multiplier=1)


def find_value(pattern, multiplier):
    duplicate_rows = find_duplicate_rows(pattern)
    mirror = find_mirror(pattern, duplicate_rows)
    if mirror:
        return multiplier * (int(mirror) + 1)
    else:
        return 0


def find_duplicate_rows(pattern):
    rows = defaultdict(list)
    for i, row in enumerate(pattern):
        rows[row].append(i)

    equal_rows = [tuple(rows[row]) for row in rows if len(rows[row]) >= 2]
    too_long = [equals for equals in equal_rows if len(equals) > 2]
    for indices in too_long:
        equal_rows.extend(combinations(indices, r=2))
        del equal_rows[equal_rows.index(indices)]
    return sorted(equal_rows)


def find_mirror(pattern, duplicate_indices):
    possible_mirrors = set()
    for i1, i2 in duplicate_indices:
        possible_mirrors.add(i1 + i2)

    for mirror in possible_mirrors:
        correct_duplicate_rows = {(x, mirror - x)
                                  for x in range(0, int(mirror / 2) + 1)
                                  if mirror - x < len(pattern)}
        relevant_duplicates = {x for x in duplicate_indices
                               if sum(x) == mirror}
        if correct_duplicate_rows == relevant_duplicates:
            return mirror / 2


def find_smudges(pattern, duplicate_indices):
    possible_mirrors = set()
    for i1, i2 in duplicate_indices:
        possible_mirrors.add(i1 + i2)

    for mirror in possible_mirrors:
        correct_duplicate_rows = {(x, mirror - x)
                                  for x in range(0, int(mirror / 2) + 1)
                                  if mirror - x < len(pattern)}
        relevant_duplicates = {x for x in duplicate_indices
                               if sum(x) == mirror}
        if len(relevant_duplicates) == len(correct_duplicate_rows) - 1:
            yield correct_duplicate_rows - relevant_duplicates

    yield {(0, 1)}
    yield {(len(pattern) - 2, len(pattern) - 1)}


def transpose(pattern):
    pattern = [''.join(col) for col in zip(*pattern)]
    return pattern


def fix_smudge(pattern, multiplier):
    duplicate_rows = find_duplicate_rows(pattern)
    possible_smudges = list(find_smudges(pattern, duplicate_rows))
    for smudge in possible_smudges:
        smudge = smudge.pop()
        row1 = int(to_bits(pattern[smudge[0]]), 2)
        row2 = int(to_bits(pattern[smudge[1]]), 2)
        try:
            diff = differs_one_spot(row1, row2)
            if diff is not None:
                return multiplier * (int((smudge[0] + smudge[1]) / 2) + 1)
        except ValueError:
            continue

def to_bits(row):
    return row.replace('#', '1').replace('.', '0')


def to_str(row, length):
    bits = bin(row)[2:]
    return bits.replace('1', '#').replace('0', '.').rjust(length, '.')


def differs_one_spot(row1, row2):
    diff = row1 ^ row2
    n = math.log(diff, 2)
    if n.is_integer():
        return int(n)


def inverted(c):
    return '.' if c == '#' else '#'


def pretty_print(pattern):
    for row in pattern:
        print(row)
