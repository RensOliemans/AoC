from itertools import combinations


def main(inputfile, part):
    with open(inputfile) as f:
        puzzle = f.read().strip()

    if part == 1:
        return part_one(puzzle)
    elif part == 2:
        return part_two(puzzle)
        

def part_one(puzzle):
    empty_rows, empty_cols = get_empty_rows_cols(puzzle)
    return sum_distance(puzzle, empty_rows, empty_cols, 1)


def part_two(puzzle):
    empty_rows, empty_cols = get_empty_rows_cols(puzzle)
    return sum_distance(puzzle, empty_rows, empty_cols, 1000000)


def get_empty_rows_cols(puzzle):
    rows = puzzle.split('\n')
    empty_rows = [index for index, row in enumerate(rows) if not '#' in row]
    
    cols = [''.join(col) for col in zip(*rows)]
    empty_cols = [index for index, col in enumerate(cols) if not '#' in col]
    
    return empty_rows, empty_cols


def sum_distance(puzzle, empty_rows, empty_cols, expansionfactor):
    from itertools import combinations
    import bisect
    
    galaxies = list()
    for x, row in enumerate(puzzle.split('\n')):
        for y, char in enumerate(row):
            if char == '#':
                galaxies.append((x, y))
    
    pairs = list(combinations(galaxies, r=2))
    
    total = 0
    for p1, p2 in pairs:
        x_min, x_max = min(p1[0], p2[0]), max(p1[0], p2[0])
        first_empty_row = bisect.bisect_left(empty_rows, x_min)
        last_empty_row = bisect.bisect_right(empty_rows, x_max)
        empty_rows_between = last_empty_row - first_empty_row
        x = x_max - x_min + empty_rows_between * (expansionfactor - 1)
    
        y_min, y_max = min(p1[1], p2[1]), max(p1[1], p2[1])
        first_empty_col = bisect.bisect_left(empty_cols, y_min)
        last_empty_col = bisect.bisect_right(empty_cols, y_max)
        empty_cols_between = last_empty_col - first_empty_col
        y = y_max - y_min + empty_cols_between * (expansionfactor - 1)
    
        total += x + y
    
    return total
