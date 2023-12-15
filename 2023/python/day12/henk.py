from functools import cache


def main(inputfile, part):
    with open(inputfile) as f:
        rows = [convert_row(line) for line in f]

    if part == 1:
        return part_one(rows)
    elif part == 2:
        return part_two(rows)


def part_one(rows):
    for row, groups in rows:
        possibilities = [count_possibilities(r, g) for r, g in rows]
        return sum(possibilities)


def part_two(rows):
    new_rows = list()
    for row, groups in rows:
        new_row = (row + '?') * 5
        new_row = new_row[:-1]
        new_groups = groups * 5
        new_rows.append((new_row, new_groups))

    for row, groups in new_rows:
        possibilities = [count_possibilities(r, g) for r, g in new_rows]
        return sum(possibilities)
    

def convert_row(line):
    r, g = line.split(' ')
    g = tuple(int(x) for x in g.split(','))
    return r, g


@cache
def count_possibilities(row, groups):
    if len(groups) == 0 and '#' not in row:
        return 1
    
    if len(row) == 0 or len(groups) == 0:
        return 0
    
    first = row[0]
    if first == '.':
        return count_possibilities(row[1:], groups)
    
    if first == '?':
        one = count_possibilities('#' + row[1:], groups)
        two = count_possibilities(row[1:], groups)
        return one + two
    
    if first == '#':
        i = 1
        try:
            while ((i < len(row) and row[i] == '#') or
                   (i < groups[0] and row[i] in ['?', '#'])):
                i += 1
        except IndexError:
            return 0
    
        if not i == groups[0]:
            return 0
    
        return count_possibilities(row[i + 1:], groups[1:])
