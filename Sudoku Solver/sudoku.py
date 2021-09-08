import copy
import pygame
from typing import Union

WHITE = (255, 255, 255)
BLACK = (0, 0, 0)

GRID1 = [[0, 9, 1, 2, 0, 0, 8, 3, 0],
         [0, 0, 0, 0, 0, 6, 0, 0, 0],
         [0, 2, 0, 0, 0, 9, 0, 4, 7],
         [0, 5, 0, 0, 0, 3, 0, 8, 0],
         [6, 0, 0, 4, 1, 0, 0, 0, 0],
         [1, 0, 0, 8, 0, 0, 0, 9, 5],
         [0, 6, 0, 9, 0, 0, 0, 2, 0],
         [0, 0, 2, 3, 0, 0, 0, 0, 4],
         [8, 3, 0, 0, 4, 0, 1, 6, 0]]
GRID2 = ([[0, 7, 0, 0, 0, 0, 0, 0, 9],
          [5, 1, 0, 4, 2, 0, 6, 0, 0],
          [0, 8, 0, 3, 0, 0, 7, 0, 0],
          [0, 0, 8, 0, 0, 1, 3, 7, 0],
          [0, 2, 3, 0, 8, 0, 0, 4, 0],
          [4, 0, 0, 9, 0, 0, 1, 0, 0],
          [9, 6, 2, 8, 0, 0, 0, 3, 0],
          [0, 0, 0, 0, 1, 0, 4, 0, 0],
          [7, 0, 0, 2, 0, 3, 0, 9, 6]])


class Solver:
    def __init__(self) -> None:
        self.width = 9
        self.grid = []
        temp = []
        for x in range(self.width):
            for y in range(self.width):
                temp.append(0)
            self.grid.append(temp)
            temp = []

    def set_grid(self, given_input: list) -> None:
        self.grid = given_input

    def set_grid_at_position(self, n: int, x: int, y: int) -> None:
        self.grid[y][x] = n

    def valid_position(self, grid: list, n: int, x_pos: int, y_pos: int) -> bool:
        if self.check_chunk(grid, self.get_chunks(x_pos, y_pos), n):
            if self.check_row(grid, n, x_pos, y_pos) and self.check_column(grid, n, x_pos, y_pos):
                return True
        return False

    def options(self, grid: list, x_pos: int, y_pos: int) -> list:
        output = []
        for i in range(1, 10):
            if self.valid_position(grid, i, x_pos, y_pos):
                output.append(i)
        return output

    def solver(self, grid: list) -> list:
        if self.completed(grid):
            return grid
        else:
            output = []
            new_grid = copy.deepcopy(grid)
            x, y = self.find_first_empty(grid)
            for o in self.options(grid, x, y):
                new_grid[y][x] = o
                output += self.solver(new_grid)
            return output

    @staticmethod
    def check_chunk(grid: list, chunk: int, n: int) -> bool:
        if chunk == 0:
            for j in range(0, 3):
                for i in range(0, 3):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 1:
            for j in range(0, 3):
                for i in range(3, 6):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 2:
            for j in range(0, 3):
                for i in range(6, 9):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 3:
            for j in range(3, 6):
                for i in range(0, 3):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 4:
            for j in range(3, 6):
                for i in range(3, 6):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 5:
            for j in range(3, 6):
                for i in range(6, 9):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 6:
            for j in range(6, 9):
                for i in range(0, 3):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 7:
            for j in range(6, 9):
                for i in range(3, 6):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        if chunk == 8:
            for j in range(6, 9):
                for i in range(6, 9):
                    temp = grid[j][i]
                    if n == temp:
                        return False
        return True

    @staticmethod
    def check_row(grid: list, n: int, x_pos: int, y_pos: int) -> bool:
        for x in range(0, 9):
            temp = grid[y_pos][x]
            if temp == n:
                return False
        return True

    @staticmethod
    def check_column(grid: list, n: int, x_pos: int, y_pos: int) -> bool:
        for y in range(0, 9):
            temp = grid[y][x_pos]
            if temp == n:
                return False
        return True

    @staticmethod
    def get_chunks(x_pos: int, y_pos: int) -> int:
        if 0 <= y_pos <= 2:
            if 0 <= x_pos <= 2:
                return 0
            elif 3 <= x_pos <= 5:
                return 1
            elif 6 <= x_pos <= 8:
                return 2
        elif 3 <= y_pos <= 5:
            if 0 <= x_pos <= 2:
                return 3
            elif 3 <= x_pos <= 5:
                return 4
            elif 6 <= x_pos <= 8:
                return 5
        elif 6 <= y_pos <= 8:
            if 0 <= x_pos <= 2:
                return 6
            elif 3 <= x_pos <= 5:
                return 7
            elif 6 <= x_pos <= 8:
                return 8

    @staticmethod
    def completed(grid: list):
        for y in range(9):
            for x in range(9):
                if grid[y][x] == 0:
                    return False
        return True

    @staticmethod
    def find_first_empty(grid: list):
        for y in range(9):
            for x in range(9):
                if grid[y][x] == 0:
                    return x, y
        return None


class Interface:
    def __init__(self, pixel_per_block: int) -> None:
        self.board = Solver()
        self.pixel_per_block = pixel_per_block
        self.size = self.pixel_per_block * 9
        self.half_block = int((self.pixel_per_block / 2) - (self.pixel_per_block / 5))
        self.run = True

        pygame.init()

        self.clock = pygame.time.Clock()
        self.font = pygame.font.SysFont('comicsans', 60)
        self.mygame = pygame.display.set_mode((self.size, self.size))
        pygame.display.set_caption("Sudoku Solver")

        self.main_loop()

    def display(self) -> None:
        self.clock.tick(60)
        self.mygame.fill(WHITE)
        for x in range(0, self.pixel_per_block * 11, self.pixel_per_block):
            pygame.draw.line(self.mygame, BLACK, (0, x), (self.size, x), 1)
        for y in range(0, self.pixel_per_block * 11, self.pixel_per_block):
            pygame.draw.line(self.mygame, BLACK, (y, 0), (y, self.size), 1)
        for x in range(0, self.pixel_per_block * 11, self.pixel_per_block * 3):
            pygame.draw.line(self.mygame, BLACK, (0, x), (self.size, x), 3)
        for y in range(0, self.pixel_per_block * 11, self.pixel_per_block * 3):
            pygame.draw.line(self.mygame, BLACK, (y, 0), (y, self.size), 3)
        for y in range(9):
            for x in range(9):
                current_num = self.board.grid[y][x]
                if current_num != 0:
                    temp = self.font.render(str(self.board.grid[y][x]), 1, BLACK)
                    self.mygame.blit(temp, (
                        self.half_block + (x * self.pixel_per_block), self.half_block + (y * self.pixel_per_block)))

        pygame.display.update()

    def main_loop(self) -> None:
        while self.run:
            key = pygame.key.get_pressed()

            for event in pygame.event.get():
                if event.type == pygame.QUIT or key[pygame.K_ESCAPE]:
                    self.run = False
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if event.button == 1:
                        x, y = event.pos
                        x, y, i, j = self.position_to_grid(x, y)
                        num = self.wait_for_input(i, j)
                        if num is not None:
                            self.board.set_grid_at_position(num, x, y)
            self.display()

    def position_to_grid(self, x_pos: int, y_pos: int) -> tuple:
        for y in range(0, 9 * self.pixel_per_block, self.pixel_per_block):
            for x in range(0, 9 * self.pixel_per_block, self.pixel_per_block):
                if x < x_pos < (x + self.pixel_per_block) and y < y_pos < (y + self.pixel_per_block):
                    return int(x / self.pixel_per_block), int(y / self.pixel_per_block), x, y

    def wait_for_input(self, x: int, y: int) -> Union[int, None]:
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                if event.type == pygame.MOUSEBUTTONDOWN:
                    return
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_0 or event.key == pygame.K_KP0:
                        return 0
                    if event.key == pygame.K_1 or event.key == pygame.K_KP1:
                        return 1
                    if event.key == pygame.K_2 or event.key == pygame.K_KP2:
                        return 2
                    if event.key == pygame.K_3 or event.key == pygame.K_KP3:
                        return 3
                    if event.key == pygame.K_4 or event.key == pygame.K_KP4:
                        return 4
                    if event.key == pygame.K_5 or event.key == pygame.K_KP5:
                        return 5
                    if event.key == pygame.K_6 or event.key == pygame.K_KP6:
                        return 6
                    if event.key == pygame.K_7 or event.key == pygame.K_KP7:
                        return 7
                    if event.key == pygame.K_8 or event.key == pygame.K_KP8:
                        return 8
                    if event.key == pygame.K_9 or event.key == pygame.K_KP9:
                        return 9

            pygame.draw.line(self.mygame, BLACK, (x, y), (x + self.pixel_per_block, y), 3)
            pygame.draw.line(self.mygame, BLACK, (x, y + self.pixel_per_block),
                             (x + self.pixel_per_block, y + self.pixel_per_block), 3)
            pygame.draw.line(self.mygame, BLACK, (x, y), (x, y + self.pixel_per_block), 3)
            pygame.draw.line(self.mygame, BLACK, (x + self.pixel_per_block, y),
                             (x + self.pixel_per_block, y + self.pixel_per_block), 3)
            pygame.display.update()


def console_print(grid):
    s = [[str(e) for e in row] for row in grid]
    lens = [max(map(len, col)) for col in zip(*s)]
    fmt = '\t'.join('{{:{}}}'.format(x) for x in lens)
    table = [fmt.format(*row) for row in s]
    print('\n'.join(table))


if __name__ == '__main__':
    player = Interface(60)

'''
#Backtrack template
def solutions(completed, options, partial = []):
    if completed(partial):
        return [partial]
    else:
        result = []
        for o in options(partial):
            augmented = partial + [o]
            result += solutions(completed, options, augmented)
        return result
'''
