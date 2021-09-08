import pygame
import math
import random
import numpy as np

PURPLE = (138, 43, 226)
LIGHT_BLUE = (0, 255, 255)
DARK_BLUE = (255, 165, 0)
ORANGE = (0, 0, 139)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
YELLOW = (255, 255, 51)
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
GREY = (150, 150, 150)
SHAPES = ["T", "I", "L", "J", "S", "Z", "O"]


class Block:
    def __init__(self, pixel_per_block, x, y):
        self.y = y
        self.x = x
        self.width = pixel_per_block
        self.height = pixel_per_block
        self.colour = BLACK

    def set_coordinates(self, x, y):
        self.x = round(x)
        self.y = round(y)

    def get_coordinates(self):
        return self.x, self.y

    def get_x(self):
        return self.x

    def get_y(self):
        return self.y

    def set_colour(self, colour):
        self.colour = colour

    def print_block(self, window):
        pygame.draw.rect(window, self.colour, (self.x, self.y, self.width, self.height))
        pygame.draw.rect(window, GREY, (self.x, self.y, self.width, self.height), 1)

    def print_block_at_next(self, window):
        pygame.draw.rect(window, self.colour,
                         (self.x + pixel_per_block * 7, self.y + pixel_per_block * 3, self.width, self.height))
        pygame.draw.rect(window, GREY,
                         (self.x + pixel_per_block * 7, self.y + pixel_per_block * 3, self.width, self.height), 1)

    def print_block_at_hold(self, window):
        pygame.draw.rect(window, self.colour,
                         (self.x + pixel_per_block * 7, self.y + pixel_per_block * 10, self.width, self.height))
        pygame.draw.rect(window, GREY,
                         (self.x + pixel_per_block * 7, self.y + pixel_per_block * 10, self.width, self.height), 1)


class Shape:
    def __init__(self, pixel_per_block, x, y):
        self.pixel_per_block = pixel_per_block
        self.pivot = Block(pixel_per_block, x, y)

    def rotate(self):
        x1, y1 = rotate(self.pivot.get_coordinates(), (self.block1.get_coordinates()), math.radians(90))
        x2, y2 = rotate(self.pivot.get_coordinates(), (self.block2.get_coordinates()), math.radians(90))
        x3, y3 = rotate(self.pivot.get_coordinates(), (self.block3.get_coordinates()), math.radians(90))
        self.block1.set_coordinates(x1, y1)
        self.block2.set_coordinates(x2, y2)
        self.block3.set_coordinates(x3, y3)

    def default_fall(self):
        self.pivot.y += self.pixel_per_block
        self.block1.y += self.pixel_per_block
        self.block2.y += self.pixel_per_block
        self.block3.y += self.pixel_per_block

    def can_move_right(self):
        if (self.pivot.x < self.pixel_per_block * 10) & (self.block1.x < self.pixel_per_block * 10) & (
                self.block2.x < self.pixel_per_block * 10) & (self.block3.x < self.pixel_per_block * 10):
            return True
        return False

    def can_move_left(self):
        if (self.pivot.x > self.pixel_per_block) & (self.block1.x > self.pixel_per_block) & (
                self.block2.x > self.pixel_per_block) & (self.block3.x > self.pixel_per_block):
            return True
        return False

    def move_right(self):
        if self.can_move_right():
            self.pivot.x += self.pixel_per_block
            self.block1.x += self.pixel_per_block
            self.block2.x += self.pixel_per_block
            self.block3.x += self.pixel_per_block

    def move_left(self):
        if self.can_move_left():
            self.pivot.x -= self.pixel_per_block
            self.block1.x -= self.pixel_per_block
            self.block2.x -= self.pixel_per_block
            self.block3.x -= self.pixel_per_block

    def print_shape(self, window):
        self.pivot.print_block(window)
        self.block1.print_block(window)
        self.block2.print_block(window)
        self.block3.print_block(window)

    def print_shape_at_next(self, window):
        self.pivot.print_block_at_next(window)
        self.block1.print_block_at_next(window)
        self.block2.print_block_at_next(window)
        self.block3.print_block_at_next(window)

    def print_shape_at_hold(self, window):
        self.pivot.print_block_at_hold(window)
        self.block1.print_block_at_hold(window)
        self.block2.print_block_at_hold(window)
        self.block3.print_block_at_hold(window)


class T_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x + pixel_per_block, y)
        self.block2 = Block(pixel_per_block, x, y + pixel_per_block)
        self.block3 = Block(pixel_per_block, x - pixel_per_block, y)
        self.pivot.set_colour(PURPLE)
        self.block1.set_colour(PURPLE)
        self.block2.set_colour(PURPLE)
        self.block3.set_colour(PURPLE)

    def print_shape(self, window):
        Shape.print_shape(self, window)


class I_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x, y - pixel_per_block)
        self.block2 = Block(pixel_per_block, x, y + pixel_per_block)
        self.block3 = Block(pixel_per_block, x, y + pixel_per_block + pixel_per_block)
        self.pivot.set_colour(LIGHT_BLUE)
        self.block1.set_colour(LIGHT_BLUE)
        self.block2.set_colour(LIGHT_BLUE)
        self.block3.set_colour(LIGHT_BLUE)

    def print_shape(self, window):
        Shape.print_shape(self, window)


class L_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x, y - pixel_per_block)
        self.block2 = Block(pixel_per_block, x + pixel_per_block, y + pixel_per_block)
        self.block3 = Block(pixel_per_block, x, y + pixel_per_block)
        self.pivot.set_colour(DARK_BLUE)
        self.block1.set_colour(DARK_BLUE)
        self.block2.set_colour(DARK_BLUE)
        self.block3.set_colour(DARK_BLUE)

    def print_shape(self, window):
        Shape.print_shape(self, window)


class J_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x, y - pixel_per_block)
        self.block2 = Block(pixel_per_block, x, y + pixel_per_block)
        self.block3 = Block(pixel_per_block, x - pixel_per_block, y + pixel_per_block)
        self.pivot.set_colour(ORANGE)
        self.block1.set_colour(ORANGE)
        self.block2.set_colour(ORANGE)
        self.block3.set_colour(ORANGE)

    def print_shape(self, window):
        Shape.print_shape(self, window)


class S_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x, y - pixel_per_block)
        self.block2 = Block(pixel_per_block, x + pixel_per_block, y - pixel_per_block)
        self.block3 = Block(pixel_per_block, x - pixel_per_block, y)
        self.pivot.set_colour(GREEN)
        self.block1.set_colour(GREEN)
        self.block2.set_colour(GREEN)
        self.block3.set_colour(GREEN)

    def print_shape(self, window):
        Shape.print_shape(self, window)


class Z_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x + pixel_per_block, y + pixel_per_block)
        self.block2 = Block(pixel_per_block, x, y + pixel_per_block)
        self.block3 = Block(pixel_per_block, x - pixel_per_block, y)
        self.pivot.set_colour(RED)
        self.block1.set_colour(RED)
        self.block2.set_colour(RED)
        self.block3.set_colour(RED)

    def print_shape(self, window):
        Shape.print_shape(self, window)


class O_Block(Shape):
    def __init__(self, pixel_per_block, x, y):
        super().__init__(pixel_per_block, x, y)
        self.block1 = Block(pixel_per_block, x - pixel_per_block, y)
        self.block2 = Block(pixel_per_block, x - pixel_per_block, y - pixel_per_block)
        self.block3 = Block(pixel_per_block, x, y - pixel_per_block)
        self.pivot.set_colour(YELLOW)
        self.block1.set_colour(YELLOW)
        self.block2.set_colour(YELLOW)
        self.block3.set_colour(YELLOW)

    def print_shape(self, window):
        Shape.print_shape(self, window)

    def rotate(self):
        pass


class Grid:
    def __init__(self, pixel_per_block):
        self.grid = []
        temp = []
        for y in range(0, pixel_per_block * 21, pixel_per_block):
            for x in range(0, pixel_per_block * 10, pixel_per_block):
                temp.append(None)
            self.grid.append(temp)
            temp = []
        for i in range(0, 10):
            self.create_block(pixel_per_block, 20, i)

    def create_block(self, pixel_per_block, x, y):
        """input x & y : pos of block e.g: 1,2,3"""
        self.grid[x][y] = Block(pixel_per_block, index_to_coor(pixel_per_block, y),
                                index_to_coor(pixel_per_block, x))

    def print_grid(self, window):
        for x in range(0, 20):
            for y in range(0, 10):
                if self.grid[x][y] is not None:
                    self.grid[x][y].print_block(window)

    def console_print(self):
        s = [[str(e) for e in row] for row in self.grid]
        lens = [max(map(len, col)) for col in zip(*s)]
        fmt = '\t'.join('{{:{}}}'.format(x) for x in lens)
        table = [fmt.format(*row) for row in s]
        print('\n'.join(table))


class Game:
    def __init__(self, pixel_per_block, window):
        self.pixel_per_block = pixel_per_block
        self.board = Grid(pixel_per_block)
        self.fall_counter = 0
        self.window = window
        self.score = 0
        self.line_counter = 0
        self.running = False
        self.game_over = False
        self.can_hold = True
        self.holding = False
        self.use_shape()
        self.generate_next_shape()

    def generate_shape(self):
        current = random.choice(SHAPES)
        if current == "T":
            shape = T_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), self.pixel_per_block)
            current_colour = PURPLE
        elif current == "I":
            shape = I_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            current_colour = LIGHT_BLUE
        elif current == "L":
            shape = L_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            current_colour = DARK_BLUE
        elif current == "J":
            shape = J_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            current_colour = ORANGE
        elif current == "S":
            shape = S_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            current_colour = GREEN
        elif current == "Z":
            shape = Z_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), self.pixel_per_block)
            current_colour = RED
        elif current == "O":
            shape = O_Block(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            current_colour = YELLOW
        return shape, current_colour

    def use_shape(self):
        shape, colour = self.generate_shape()
        self.shape = shape
        self.current_colour = colour

    def generate_next_shape(self):
        shape, colour = self.generate_shape()
        self.next_shape = shape
        self.next_colour = colour

    def can_fall(self):
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.pivot.get_coordinates()[1]) + self.pixel_per_block)][
                    coor_to_index(self.pixel_per_block, self.shape.pivot.get_coordinates()[0])] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block1.get_coordinates()[1]) + self.pixel_per_block)][
                    coor_to_index(self.pixel_per_block, self.shape.block1.get_coordinates()[0])] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block2.get_coordinates()[1]) + self.pixel_per_block)][
                    coor_to_index(self.pixel_per_block, self.shape.block2.get_coordinates()[0])] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block3.get_coordinates()[1]) + self.pixel_per_block)][
                    coor_to_index(self.pixel_per_block, self.shape.block3.get_coordinates()[0])] is not None:
            return False
        return True

    def place_block(self):
        if not self.can_fall():
            self.board.create_block(self.pixel_per_block, coor_to_index(self.pixel_per_block, self.shape.pivot.get_y()),
                                    coor_to_index(self.pixel_per_block, self.shape.pivot.get_x()))
            self.board.grid[coor_to_index(self.pixel_per_block, self.shape.pivot.get_y())][
                coor_to_index(self.pixel_per_block, self.shape.pivot.get_x())].set_colour(self.current_colour)
            self.board.create_block(self.pixel_per_block,
                                    coor_to_index(self.pixel_per_block, self.shape.block1.get_y()),
                                    coor_to_index(self.pixel_per_block, self.shape.block1.get_x()))
            self.board.grid[coor_to_index(self.pixel_per_block, self.shape.block1.get_y())][
                coor_to_index(self.pixel_per_block, self.shape.block1.get_x())].set_colour(self.current_colour)
            self.board.create_block(self.pixel_per_block,
                                    coor_to_index(self.pixel_per_block, self.shape.block2.get_y()),
                                    coor_to_index(self.pixel_per_block, self.shape.block2.get_x()))
            self.board.grid[coor_to_index(self.pixel_per_block, self.shape.block2.get_y())][
                coor_to_index(self.pixel_per_block, self.shape.block2.get_x())].set_colour(self.current_colour)
            self.board.create_block(self.pixel_per_block,
                                    coor_to_index(self.pixel_per_block, self.shape.block3.get_y()),
                                    coor_to_index(self.pixel_per_block, self.shape.block3.get_x()))
            self.board.grid[coor_to_index(self.pixel_per_block, self.shape.block3.get_y())][
                coor_to_index(self.pixel_per_block, self.shape.block3.get_x())].set_colour(self.current_colour)
            self.shape = self.next_shape
            self.current_colour = self.next_colour
            self.generate_next_shape()
            self.can_hold = True

    def default_fall(self):
        if self.can_fall():
            self.fall_counter += 1
            if self.fall_counter >= 20:
                self.shape.default_fall()
                self.fall_counter = 0
        else:
            self.fall_counter += 1
            if self.fall_counter >= 20:
                self.place_block()

    def can_right(self):
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              self.shape.pivot.get_y())][
                    coor_to_index(self.pixel_per_block, self.shape.pivot.get_x() + self.pixel_per_block)] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block1.get_y()))][
                    coor_to_index(self.pixel_per_block, self.shape.block1.get_x() + self.pixel_per_block)] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block2.get_y()))][
                    coor_to_index(self.pixel_per_block, self.shape.block2.get_x() + self.pixel_per_block)] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block3.get_y()))][
                    coor_to_index(self.pixel_per_block, self.shape.block3.get_x() + self.pixel_per_block)] is not None:
            return False
        return True

    def can_left(self):
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              self.shape.pivot.get_y())][
                    coor_to_index(self.pixel_per_block, self.shape.pivot.get_x() - self.pixel_per_block)] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block1.get_y()))][
                    coor_to_index(self.pixel_per_block, self.shape.block1.get_x() - self.pixel_per_block)] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block2.get_y()))][
                    coor_to_index(self.pixel_per_block, self.shape.block2.get_x() - self.pixel_per_block)] is not None:
            return False
        if \
                self.board.grid[coor_to_index(self.pixel_per_block,
                                              (self.shape.block3.get_y()))][
                    coor_to_index(self.pixel_per_block, self.shape.block3.get_x() - self.pixel_per_block)] is not None:
            return False
        return True

    def hard_fall(self):
        while self.can_fall():
            self.fall_counter = 0
            self.shape.default_fall()
        self.place_block()

    def can_rotate(self):
        x1, y1 = rotate(self.shape.pivot.get_coordinates(), (self.shape.block1.get_coordinates()), math.radians(90))
        x2, y2 = rotate(self.shape.pivot.get_coordinates(), (self.shape.block2.get_coordinates()), math.radians(90))
        x3, y3 = rotate(self.shape.pivot.get_coordinates(), (self.shape.block3.get_coordinates()), math.radians(90))
        x1 = coor_to_index(self.pixel_per_block, x1)
        x2 = coor_to_index(self.pixel_per_block, x2)
        x3 = coor_to_index(self.pixel_per_block, x3)
        y1 = coor_to_index(self.pixel_per_block, y1)
        y2 = coor_to_index(self.pixel_per_block, y2)
        y3 = coor_to_index(self.pixel_per_block, y3)
        if 0 <= x1 <= 9 and 0 <= x2 <= 9 and 0 <= x3 <= 9 and 0 <= y1 <= 19 and 0 <= y2 <= 19 and 0 <= y3 <= 19:
            in_bounds = True
        else:
            in_bounds = False
        if in_bounds:
            if (self.board.grid[y1][x1] is None) & (self.board.grid[y2][x2] is None) & (
                    self.board.grid[y3][x3] is None):
                return True
        else:
            return False

    def clear_line(self, cleared_line):
        self.line_counter += 1
        for current_line in range(cleared_line - 1, -1, -1):
            self.drop_line(current_line)
        self.board.grid[0] = [None, None, None, None, None, None, None, None, None, None]

    def drop_line(self, line):
        temp = []
        for x in range(0, 10):
            temp.append(self.board.grid[line][x])
        self.board.grid[line + 1] = temp

        for current_block in self.board.grid[line + 1]:
            if current_block is not None:
                x, y = current_block.get_coordinates()
                y = y + self.pixel_per_block
                current_block.set_coordinates(x, y)

    def clear_check(self):
        self.line_counter = 0
        for x in range(0, 20):
            check = True
            for y in range(0, 10):
                if self.board.grid[x][y] is None:
                    check = False
            if check:
                self.clear_line(x)
        if 0 < self.line_counter < 4:
            self.score += self.line_counter * 100
        elif self.line_counter >= 4:
            self.score += 800

    def game_over_check(self):
        if self.board.grid[0][5] is not None:
            self.running = False
            self.game_over = True

    def hold(self):
        if not self.holding and self.can_hold:
            self.shape.__init__(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            self.holding = True
            self.hold_shape = self.shape
            self.hold_colour = self.current_colour
            self.shape = self.next_shape
            self.current_colour = self.next_colour
            self.generate_next_shape()
        elif self.holding and self.can_hold:
            self.shape.__init__(self.pixel_per_block, index_to_coor(self.pixel_per_block, 5), 2 * self.pixel_per_block)
            self.shape, self.hold_shape = self.hold_shape, self.shape
            self.current_colour, self.hold_colour = self.hold_colour, self.current_colour
        self.can_hold = False


def coor_to_index(pixel_per_block, n):
    return round((n - pixel_per_block) / pixel_per_block)


def index_to_coor(pixel_per_block, n):
    return round(pixel_per_block + pixel_per_block * n)


def rotate(origin, point, angle):
    """
    Rotate a point counterclockwise by a given angle around a given origin.

    The angle should be given in radians.
    """
    ox, oy = origin
    px, py = point

    qx = ox + math.cos(angle) * (px - ox) - math.sin(angle) * (py - oy)
    qy = oy + math.sin(angle) * (px - ox) + math.cos(angle) * (py - oy)
    return qx, qy


def game_display(window):
    clock = pygame.time.Clock()
    clock.tick(20)
    mygame.fill((0, 0, 0))
    font = pygame.font.SysFont('comicsans', 50)
    text = font.render('Score: ' + str(player.score), 1, WHITE)
    mygame.blit(text, (pixel_per_block * 12, pixel_per_block * 1))
    for x in range(pixel_per_block, pixel_per_block * 12, pixel_per_block):
        pygame.draw.line(window, WHITE, (x, pixel_per_block), (x, pixel_per_block * 21))
    for y in range(pixel_per_block, pixel_per_block * 22, pixel_per_block):
        pygame.draw.line(window, WHITE, (pixel_per_block, y), (pixel_per_block * 11, y))
    font = pygame.font.SysFont('comicsans', 50)
    text = font.render('Next :', 1, WHITE)
    mygame.blit(text, (pixel_per_block * 12, pixel_per_block * 3))
    pygame.draw.line(window, WHITE, (pixel_per_block * 12, pixel_per_block * 4),
                     (pixel_per_block * 15, pixel_per_block * 4))
    pygame.draw.line(window, WHITE, (pixel_per_block * 12, pixel_per_block * 8),
                     (pixel_per_block * 15, pixel_per_block * 8))
    pygame.draw.line(window, WHITE, (pixel_per_block * 12, pixel_per_block * 4),
                     (pixel_per_block * 12, pixel_per_block * 8))
    pygame.draw.line(window, WHITE, (pixel_per_block * 15, pixel_per_block * 4),
                     (pixel_per_block * 15, pixel_per_block * 8))
    player.next_shape.print_shape_at_next(mygame)

    font = pygame.font.SysFont('comicsans', 50)
    text = font.render('Hold :', 1, WHITE)
    mygame.blit(text, (pixel_per_block * 12, pixel_per_block * 10))
    pygame.draw.line(window, WHITE, (pixel_per_block * 12, pixel_per_block * 11),
                     (pixel_per_block * 15, pixel_per_block * 11))
    pygame.draw.line(window, WHITE, (pixel_per_block * 12, pixel_per_block * 15),
                     (pixel_per_block * 15, pixel_per_block * 15))
    pygame.draw.line(window, WHITE, (pixel_per_block * 12, pixel_per_block * 11),
                     (pixel_per_block * 12, pixel_per_block * 15))
    pygame.draw.line(window, WHITE, (pixel_per_block * 15, pixel_per_block * 11),
                     (pixel_per_block * 15, pixel_per_block * 15))
    if player.holding:
        player.hold_shape.print_shape_at_hold(mygame)


def pre_game_display(window):
    clock = pygame.time.Clock()
    clock.tick(20)
    mygame.fill((0, 0, 0))
    font = pygame.font.SysFont('couriernew', 100)
    title_text = font.render('TETRIS', 1, WHITE)
    font = pygame.font.SysFont('couriernew', 40)
    start_text = font.render('Press ENTER to start', 1, WHITE)
    mygame.blit(title_text, (pixel_per_block * 4, pixel_per_block * 4))
    mygame.blit(start_text, (pixel_per_block * 3, pixel_per_block * 15))


def game_over_display(window):
    clock = pygame.time.Clock()
    clock.tick(20)
    mygame.fill((0, 0, 0))
    font = pygame.font.SysFont('couriernew', 100)
    title_text = font.render('GAME OVER!', 1, WHITE)
    font = pygame.font.SysFont('couriernew', 80)
    score_text = font.render('Your Score: ', 1, WHITE)
    font = pygame.font.SysFont('couriernew', 100)
    score = font.render(str(player.score), 1, WHITE)
    font = pygame.font.SysFont('couriernew', 40)
    start_text = font.render('Press enter to continue', 1, WHITE)
    mygame.blit(title_text, (pixel_per_block * 1, pixel_per_block * 4))
    mygame.blit(score_text, (pixel_per_block * 1, pixel_per_block * 7))
    mygame.blit(score, (pixel_per_block * 1, pixel_per_block * 10))
    mygame.blit(start_text, (pixel_per_block * 2, pixel_per_block * 15))


if __name__ == "__main__":

    pixel_per_block = 40
    run = True
    fall_counter = 0
    pygame.init()
    mygame = pygame.display.set_mode((pixel_per_block * 17, pixel_per_block * 22))
    pygame.display.set_caption("Tetris")

    while run:
        player = Game(pixel_per_block, mygame)
        pre_game_display(mygame)
        key = pygame.key.get_pressed()

        for event in pygame.event.get():
            if event.type == pygame.QUIT or key[pygame.K_ESCAPE]:
                run = False
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_RETURN:
                    player.running = True

        pygame.display.update()

        while player.running:
            fall_counter += 1
            key = pygame.key.get_pressed()
            game_display(mygame)
            player.shape.print_shape(mygame)
            player.default_fall()

            for event in pygame.event.get():
                if event.type == pygame.QUIT or key[pygame.K_ESCAPE]:
                    run = False
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_UP:
                        if player.can_rotate():
                            player.shape.rotate()
                    if event.key == pygame.K_DOWN:
                        if player.can_fall():
                            player.shape.default_fall()
                    if event.key == pygame.K_LEFT:
                        if player.shape.can_move_left():
                            if player.can_left():
                                player.shape.move_left()
                    if event.key == pygame.K_RIGHT:
                        if player.shape.can_move_right():
                            if player.can_right():
                                player.shape.move_right()
                    if event.key == pygame.K_SPACE:
                        player.hard_fall()
                    if event.key == pygame.K_c:
                        player.hold()

            if not run:
                break

            player.clear_check()
            player.board.print_grid(mygame)
            player.game_over_check()
            pygame.display.update()

        while player.game_over:
            key = pygame.key.get_pressed()
            game_over_display(mygame)
            pygame.display.update()

            for event in pygame.event.get():
                if event.type == pygame.QUIT or key[pygame.K_ESCAPE]:
                    player.game_over = False
                    run = False
                elif event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_RETURN:
                        player.game_over = False

    pygame.quit()
