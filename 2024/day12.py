import time
from collections import defaultdict, namedtuple


Element = namedtuple('Element', ['x', 'y', 'char'])


class TwoDGrid:
    def __init__(self, s: str):
        self._lines = s.strip().split()
        self.x = len(self._lines)
        self.y = len(self._lines[0])
        assert all(len(line) == self.y for line in self._lines), "All lines must have the same length"

        self._idx = [0, -1]

    def __str__(self):
        return "\n".join(self._lines)

    def __repr__(self):
        return f"<TwoDGrid [{self.x}x{self.y}]>"

    def __getitem__(self, key: tuple):
        x, y = key
        if 0 <= x < self.x and 0 <= y < self.y:
            return Element(x, y, self._lines[x][y])
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


def neighbours(grid: TwoDGrid, element: element):
    return filter(lambda el: el is not None, none_neighbours(grid, element))


def none_neighbours(grid, element):
    ns = []
    for direction in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
        x, y = element.x + direction[0], element.y + direction[1]
        ns.append(grid[x, y])
    return ns


def find_contiguous_group(grid: TwoDGrid, el: Element, group = None):
    group = group or set()

    if el in group:
        return group
    group.add(el)

    ns = list(neighbours(grid, el))
    ns = filter(lambda n: n.char == el.char, ns)
    for neighbour in ns:
        group.update(find_contiguous_group(grid, neighbour, group))

    return group


def find_all_groups(grid):
    groups = set()
    grouped = set()
    for el in grid:
        if el in grouped:
            continue
        group = find_contiguous_group(grid, el)
        grouped.update(group)
        groups.add(frozenset(group))

    return groups


def p1(s):
    grid = TwoDGrid(s)
    groups = find_all_groups(grid)
    total = 0
    for group in groups:
        perimeter = 0
        for el in group:
            ns = none_neighbours(grid, el)
            different_neighbours = filter(lambda n: n is None or n.char != el.char, ns)
            perimeter += len(list(different_neighbours))
        price = perimeter * len(group)
        total += price
    return total

def p2(s):
    pass


ex1 = """AAAA
BBCD
BBCC
EEEC"""
assert 140 == p1(ex1)

assert 772 == p1("""OOOOO
OXOXO
OOOOO
OXOXO
OOOOO
""")

assert 1930 == p1("""RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
""")

start = time.time()
print(p1(open("inputs/12").read()))
print(f"{time.time() - start:.2f}s")

print(p2(ex1))
