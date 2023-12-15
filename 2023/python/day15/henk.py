from collections import defaultdict, OrderedDict

def main(inputfile, part):
    with open(inputfile) as f:
        initialization_sequence = f.read().strip()

    if part == 1:
        return part_one(initialization_sequence)
    elif part == 2:
        return part_two(initialization_sequence)


def part_one(initialization_sequence):
    return sum(map(holiday_ascii_string_helper, initialization_sequence.split(',')))


def holiday_ascii_string_helper(string):
    current = 0
    for c in string:
        current += ord(c)
        current *= 17
        current = current % 256
    return current


def part_two(initialization_sequence):
    boxes = holiday_ascii_string_helper_manual_arrangement_proceduce(initialization_sequence)
    total = 0
    for box in boxes:
        for i, (label, focal_length) in enumerate(boxes[box].items()):
            total += (box + 1) * (i + 1) * focal_length
    return total


def holiday_ascii_string_helper_manual_arrangement_proceduce(initialization_sequence):
    boxes = defaultdict(OrderedDict)
    for step in initialization_sequence.split(','):
        if '-' in step:
            label = step[:-1]
            box = boxes[holiday_ascii_string_helper(label)]
            if label in box:
                del box[label]
        else:
            label, focal_length = step.split('=')
            boxes[holiday_ascii_string_helper(label)][label] = int(focal_length)

    return boxes
