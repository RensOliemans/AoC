def main(inputfile, part):
    with open(inputfile) as f:
        grid = f.read().strip().split('\n')

    if part == 1:
        return part_one(grid)
    elif part == 2:
        return part_two(grid)
        

def part_one(grid):
    S = find_start_location(grid)
    path = traverse_path(grid, S)
    return len(path) // 2


def part_two(grid):
    S = find_start_location(grid)
    path = traverse_path(grid, S)

    counter = 0
    for i, row in enumerate(grid):
        pipe_count = 0
        for j, char in enumerate(row):
            if (i, j) in path and char in ['|', 'J', 'L', 'S']:
                pipe_count += 1
            elif pipe_count % 2 == 1 and (i, j) not in path:
                counter += 1

    return counter


def find_start_location(grid):
    for y, row in enumerate(grid):
        for x, char in enumerate(row):
            if char == 'S':
                return (y, x)


def traverse_path(grid, initial_cell):
    """Returns the length of the path traversed by starting from cell in grid."""
    import random
    
    def find_adjacent_cells(grid, cell):
        """Determines what two cells are connected to one cell."""
        y, x = cell[0], cell[1]
        char = grid[y][x]
        match char:
            case "S":
                possible_neighbours = {
                    (y + 1, x) if y < len(grid) else (y - 1, x),
                    (y - 1, x) if y > 0 else (y + 1, x),
                    (y, x + 1) if x < len(grid[0]) else (y, x - 1),
                    (y, x - 1) if x > 0 else (y, x + 1),
                }
                
                neighbours = list()
                for neighbour in possible_neighbours:
                    cells = find_adjacent_cells(grid, neighbour)
                    if cells is not None and (y, x) in cells:
                        neighbours.append(neighbour)
                
                assert len(neighbours) == 2, f"{len(possible_neighbours)} should be equal to 2"
                return neighbours
            case "|":
                return (y - 1, x), (y + 1, x)
            case "-":
                return (y, x - 1), (y, x + 1)
            case "F":
                return (y + 1, x), (y, x + 1)
            case "7":
                return (y + 1, x), (y, x - 1)
            case "L":
                return (y - 1, x), (y, x + 1)
            case "J":
                return (y - 1, x), (y, x - 1)
    
    path = [initial_cell]
    start_cells = find_adjacent_cells(grid, initial_cell)
    cell_to_traverse = random.choice(start_cells)
    
    previous_cell = initial_cell
    while cell_to_traverse != initial_cell:
        path.append(cell_to_traverse)
        adjacent_cells = find_adjacent_cells(grid, cell_to_traverse)
        temp = cell_to_traverse
        cell_to_traverse = [cell for cell in adjacent_cells
                            if cell != previous_cell][0]
        previous_cell = temp
    
    return path


