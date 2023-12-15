def main(inputfile, part):
    with open(inputfile) as f:
        lines = [[int(c) for c in line.split(' ')]
                 for line in f]

    part = part_one if part == 1 else part_two

    return part(lines)


def part_one(lines):
    total = 0
    for line in lines:
        tree = difference_tree(line)
        for i in reversed(range(len(tree) - 1)):
            tree[i].append(tree[i][-1] + tree[i + 1][-1])

        total += tree[0][-1]

    return total


def part_two(lines):
    total = 0
    for line in lines:
        tree = difference_tree(line)
        for i in reversed(range(len(tree) - 1)):
            tree[i].insert(0, tree[i][0] - tree[i + 1][0])

        total += tree[0][0]

    return total


def difference_tree(line):
    tree = [line]
    tree.append([line[i + 1] - line[i] for i in range(len(line) - 1)])
    while not all((diff == 0 for diff in tree[-1])):
        tree.append([tree[-1][i + 1] - tree[-1][i]
                     for i in range(len(tree[-1]) - 1)])

    return tree

