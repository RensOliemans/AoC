import math


def main(inputfile, part):
    with open(inputfile) as f:
        puzzle = f.read()

    part = part_one if part == 1 else part_two

    instructions, network = puzzle.split('\n\n')
    return part(instructions, network_dict(network))


def infinite_instructions(instructions):
    i = 0
    while True:
        if i == len(instructions):
            i = 0

        instruction = instructions[i]
        yield 0 if instruction == 'L' else 1
        i += 1


def network_dict(network):
    result = dict()
    for line in network.strip().split('\n'):
        origin, edges = line.split(' = ')
        edges = edges.replace('(', '').replace(')', '').split(', ')
        result[origin] = edges

    return result


def part_one(instructions, network):
    final_node_fn = lambda node: node == 'ZZZ'
    return path_length(instructions, network, 'AAA', final_node_fn)


def path_length(instructions, network, initial_node, final_node_fn):
    instructions = infinite_instructions(instructions)
    next_node = network[initial_node][next(instructions)]
    i = 1
    while not final_node_fn(next_node):
        next_node = network[next_node][next(instructions)]
        i += 1
    return i


def part_two(instructions, network):
    initial_nodes = [node for node in network.keys() if node.endswith('A')]
    final_node_fn = lambda node: node.endswith('Z')
    lengths = list()
    for node in initial_nodes:
        lengths.append(path_length(instructions, network, node, final_node_fn))

    return math.lcm(*lengths)
