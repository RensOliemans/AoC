def main(inputfile, part):
    with open(inputfile) as f:
        puzzle = f.read()

    part = part_one if part == 1 else part_two

    return part(puzzle)


def part_one(puzzle):
    return 1


def part_two(puzzle):
    return 2
