def main(inputfile, part):
    with open(inputfile) as f:
        platform = [list(l.strip()) for l in f]

    platform = roll(platform)

    if part == 1:
        return part_one(platform)


def roll(platform):
    # We're mutating a list which isn't really nice, but since we are returning
    # it anyway and not using the original value I'm OK with it.
    for i, row in enumerate(platform):
        if i == 0:
            continue
        for j, char in enumerate(platform[i]):
            if char == 'O':
                above = 0
                while above < i and platform[i - (above + 1)][j] == '.':
                    above += 1

                if platform[i - above][j] == '.':
                    platform[i - above][j] = 'O'
                    platform[i][j] = '.'

    return platform


def part_one(platform):
    total = 0
    for i, line in enumerate(platform):
        #print(f"{''.join(line)} {str(len(platform) - i).rjust(2, ' ')}")
        total += len([r for r in line if r == 'O']) * (len(platform) - i)

    return total


print()
print(main("input.txt", 1))
