from collections import namedtuple
from enum import Enum, auto

class Direction(Enum):
    NORTH = 0
    EAST = 1
    SOUTH = 2
    WEST = 3


Location = namedtuple('Location', ['x', 'y'])
State = namedtuple('State', ['location', 'direction'])


def main(inputfile, part):
    with open(inputfile) as f:
        grid = [list(l) for l in f.read().strip().split('\n')]
        grid = pad_grid(grid)

    part = part_one if part == 1 else part_two

    return part(grid)


def part_one(grid):
    initial_state = State(Location(1, 1), Direction.EAST)
    return energized_tiles(grid, initial_state)


def part_two(grid):
    energized_counts = dict()
    for i, row in enumerate(grid):
        initial_state = State(Location(i, 1), Direction.SOUTH)
        energized_counts[initial_state] = energized_tiles(grid, initial_state)

        initial_state = State(Location(i, len(grid) - 2), Direction.NORTH)
        energized_counts[initial_state] = energized_tiles(grid, initial_state)

        initial_state = State(Location(1, i), Direction.EAST)
        energized_counts[initial_state] = energized_tiles(grid, initial_state)

        initial_state = State(Location(len(grid) - 2, i), Direction.WEST)
        energized_counts[initial_state] = energized_tiles(grid, initial_state)

    return max(energized_counts.values())


def energized_tiles(grid, initial_state):
    next_states = list(next_states_of_state(grid, initial_state))
    visited_states = {initial_state}
    while len(next_states) > 0:
        next_state = next_states.pop(0)
        if next_state in visited_states:
            continue

        visited_states.add(next_state)
        x, y = next_state.location
        next_states.extend(list(next_states_of_state(grid, next_state)))

    visited_locations = {s.location for s in visited_states}
    return len(visited_locations)


def next_states_of_state(grid, state):
    x, y = state.location

    new_tile = grid[y][x]

    direction = state.direction
    directions = [direction]
    match new_tile:
        case '/':
            if direction == Direction.NORTH or direction == Direction.SOUTH:
                directions = [Direction(direction.value + 1)]
            else:
                directions = [Direction(direction.value - 1)]
        case '\\':
            directions = [Direction(3 - direction.value)]
        case '-':
            if direction == Direction.NORTH or direction == Direction.SOUTH:
                directions = [Direction.EAST, Direction.WEST]
        case '|':
            if direction == Direction.EAST or direction == Direction.WEST:
                directions = [Direction.NORTH, Direction.SOUTH]
        case _:
            directions = [direction]

    for direction in directions:
        new_x, new_y = new_x_ys(State(Location(x, y), direction))
        if grid[new_y][new_x] == 'X':
            continue

        yield State(Location(new_x, new_y), direction)


def new_x_ys(state):
    x, y = state.location
    match state.direction:
        case Direction.NORTH:
            return x, y - 1
        case Direction.SOUTH:
            return x, y + 1
        case Direction.WEST:
            return x - 1, y
        case Direction.EAST:
            return x + 1, y

def pad_grid(grid):
    l, w = len(grid), len(grid[0])
    for r in grid:
        r.insert(0, 'X')
        r.append('X')

    grid.insert(0, ['X' for _ in range(w + 2)])
    grid.append(['X' for _ in range(w + 2)])

    return grid


def pretty_print(grid):
    print('\n'.join(''.join(r) for r in grid))
