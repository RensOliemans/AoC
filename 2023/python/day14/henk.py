import time
from collections import defaultdict
from copy import deepcopy
from enum import Enum


class Direction(Enum):
    NORTH = 0
    WEST = 1
    SOUTH = 2
    EAST = 3


def main(inputfile, part):
    with open(inputfile) as f:
        platform = [list(l.strip()) for l in f]

    platform = pad(platform)

    if part == 1:
        return part_one(platform)
    elif part == 2:
        return part_two(platform)


def part_one(platform):
    platform = roll(platform, Direction.NORTH)

    platform = remove_pad(platform)
    return north_load(platform)


def part_two(platform, cycles=1000000000, recurse=True):
    previous = list()
    i = 0
    rolls = cycles * 4
    for i in range(rolls):
        direction = Direction(i % 4)
        platform = roll(platform, direction)
        if recurse and platform in previous and direction == Direction.EAST:
            index = previous.index(platform)
            remainder = (rolls - i - 1) % (i - index)
            return part_two(platform, remainder // 4, False)

        previous.append(deepcopy(platform))

    platform = remove_pad(platform)
    return north_load(platform)


def pad(platform):
    l, w = len(platform), len(platform[0])
    for r in platform:
        r.insert(0, 'X')
        r.append('X')

    platform.insert(0, ['X' for _ in range(w + 2)])
    platform.append(['X' for _ in range(w + 2)])

    return platform


def remove_pad(platform):
    platform.pop()
    platform.pop(0)
    w = len(platform[0])
    for r in platform:
        r.pop()
        r.pop(0)

    return platform


def north_load(platform):
    total = 0
    for i, line in enumerate(platform):
        #print(f"{''.join(line)} {str(len(platform) - i).rjust(2, ' ')}")
        total += len([r for r in line if r == 'O']) * (len(platform) - i)

    return total


def roll(platform, direction):
    # All roll methods update the platform list in-place.
    match direction:
        case direction.NORTH:
            roll_north(platform)
        case direction.EAST:
            roll_east(platform)
        case direction.SOUTH:
            roll_south(platform)
        case direction.WEST:
            roll_west(platform)

    return platform


# Warning: fantastic solution below
def roll_north(platform):
    for i, row in enumerate(platform):
        for j, char in enumerate(platform[i]):
            if char == 'O':

                adjacent = 0
                while platform[i - (adjacent + 1)][j] == '.':
                    adjacent += 1

                if platform[i - adjacent][j] == '.':
                    platform[i - adjacent][j] = 'O'
                    platform[i][j] = '.'


def roll_west(platform):
    for i, row in enumerate(platform):
        for j, char in enumerate(platform[i]):
            if char == 'O':

                adjacent = 0
                while platform[i][j - (adjacent + 1)] == '.':
                    adjacent += 1

                if platform[i][j - adjacent] == '.':
                    platform[i][j - adjacent] = 'O'
                    platform[i][j] = '.'


def roll_east(platform):
    for i, row in enumerate(platform):
        for j in range(len(platform[i]) - 1, 0, -1):
            char = row[j]
            if char == 'O':

                adjacent = 0
                while platform[i][j + adjacent + 1] == '.':
                    adjacent += 1

                if platform[i][j + adjacent] == '.':
                    platform[i][j + adjacent] = 'O'
                    platform[i][j] = '.'


def roll_south(platform):
    for i in range(len(platform) - 1, 0, -1):
        row = platform[i]
        for j, char in enumerate(platform[i]):
            if char == 'O':

                adjacent = 0
                while platform[i + adjacent + 1][j] == '.':
                    adjacent += 1

                if platform[i + adjacent][j] == '.':
                    platform[i + adjacent][j] = 'O'
                    platform[i][j] = '.'


def pretty_print(platform):
    for row in platform:
        print(''.join(row))
