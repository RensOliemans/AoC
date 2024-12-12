import time
from collections import defaultdict, namedtuple


Element = namedtuple('Element', ['x', 'y', 'char'])


class TwoDGrid:
    def __init__(self, s: str):
        self._lines = s.strip().split()
        self.y = len(self._lines)
        self.x = len(self._lines[0])
        assert all(len(line) == self.x for line in self._lines), "All lines must have the same length"

        self._idx = [0, -1]

    def __str__(self):
        return "\n".join(self._lines)

    def __repr__(self):
        return f"<TwoDGrid [{self.x}x{self.y}]>"

    def __getitem__(self, key: tuple):
        y, x = key
        if 0 <= x < self.x and 0 <= y < self.y:
            return Element(x, y, self._lines[y][x])
        else:
            return None

    def __iter__(self):
        return self

    def __next__(self):
        if self._idx[0] == self.x - 1 and self._idx[1] == self.y - 1:
            raise StopIteration

        if self._idx[1] == self.y - 1:
            self._idx[1] = 0
            self._idx[0] += 1
        else:
            self._idx[1] += 1

        return self[self._idx]

    def __len__(self):
        return self.x * self.y


cardinal_directions = [(0, -1), (1, 0), (0, 1), (-1, 0)]


def neighbours(grid: TwoDGrid, element: element):
    return filter(lambda el: el is not None, none_neighbours(grid, element))


def none_neighbours(grid, element):
    ns = []
    for direction in cardinal_directions:
        x, y = element.x + direction[0], element.y + direction[1]
        ns.append(grid[y, x])
    return ns


def find_contiguous_group(grid: TwoDGrid, el: Element, group = None):
    group = group or set()

    if el in group:
        return group
    group.add(el)

    same_neighbours = (neighbour for neighbour in neighbours(grid, el)
                       if neighbour.char == el.char)
    for neighbour in same_neighbours:
        group.update(find_contiguous_group(grid, neighbour, group))

    return group


def find_all_groups(grid):
    groups = set()
    already_grouped = set()
    for el in grid:
        if el in already_grouped:
            continue
        group = find_contiguous_group(grid, el)
        groups.add(frozenset(group))
        already_grouped.update(group)

    return groups


def group_perimeter(grid, group):
    perimeter = 0
    for el in group:
        ns = none_neighbours(grid, el)
        different_neighbours = [n for n in ns if n is None or n.char != el.char]
        perimeter += len(different_neighbours)
    return perimeter


def p1(s):
    grid = TwoDGrid(s)
    groups = find_all_groups(grid)
    return sum([len(group) * group_perimeter(grid, group)
                for group in groups])


def group_sides(grid, group):
    xs = set()
    ys = set()
    all_sides = defaultdict(set)
    for el in group:
        ns = none_neighbours(grid, el)
        ns = [(direction, n) for (direction, n) in enumerate(ns)
              if n is None or n.char != el.char]
        different_neighbours = [(d, n) for (d, n) in ns
                                if n is None or n.char != el.char]
        for d, n in different_neighbours:
            all_sides[el].add(d)

    dupes = 0
    for el in all_sides.keys():
        directions = all_sides[el]

        ns = neighbours(grid, el)
        same_neighbours = (neighbour for neighbour in ns
                           if neighbour.char == el.char)

        for neigh in same_neighbours:
            dupes += len(directions.intersection(all_sides.get(neigh, [])))

    return sum([len(all_sides[k]) for k in all_sides.keys()]) - dupes // 2


def p2(s):
    grid = TwoDGrid(s)
    group = find_contiguous_group(grid, Element(0, 0, 'A'))
    group = find_contiguous_group(grid, Element(3, 3, 'C'))
    groups = find_all_groups(grid)

    return sum([len(group) * group_sides(grid, group)
                for group in groups])


ex1 = """AAAA
BBCD
BBCC
EEEC"""

ex2 = """OOOOO
OXOXO
OOOOO
OXOXO
OOOOO
"""

ex3 = """RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
"""

assert 140 == p1(ex1)
assert 772 == p1(ex2)
assert 1930 == p1(ex3)

with open("inputs/12") as f:
    puzzle = f.read()

start = time.time()
print(p1(puzzle))
print(f"{time.time() - start:.2f}s")
print()

assert 80 == p2(ex1)
assert 436 == p2(ex2)
assert 1206 == p2(ex3)

start = time.time()
print(p2(puzzle))
print(f"{time.time() - start:.2f}s")
