import pygame
import random


class Block:
    def __init__(self, x, y):
        self.width = 20
        self.height = 20
        self.x = x
        self.y = y

    def get_block(self):
        return self.x, self.y

    def get_all(self):
        return self.x, self.y, self.width, self.height

    def get_prev(self):
        return self.px, self.py

    def move_left(self):
        self.px = self.x
        self.py = self.y
        self.x -= 20

    def move_right(self):
        self.px = self.x
        self.py = self.y
        self.x += 20

    def move_up(self):
        self.px = self.x
        self.py = self.y
        self.y -= 20

    def move_down(self):
        self.px = self.x
        self.py = self.y
        self.y += 20

    def change_direction(self, input):
        if input == "R":
            self.direction = "R"
        elif input == "L":
            self.direction = "L"
        elif input == "U":
            self.direction = "U"
        elif input == "D":
            self.direction = "D"

    def move(self):
        if self.direction == "R":
            self.move_right()
        elif self.direction == "L":
            self.move_left()
        elif self.direction == "U":
            self.move_up()
        elif self.direction == "D":
            self.move_down()

    def follow(self, front):
        self.px = self.x
        self.py = self.y
        self.x = front.px
        self.y = front.py

    def show_me(self, window):
        pygame.draw.rect(window, (255, 255, 255), self.get_all())


class Snake:
    def __init__(self, x, y):
        self.head = Block(x, y)
        self.head.change_direction("R")
        self.body = [self.head]
        self.length = 1
        self.food = Food(self.head.width * 25, self.head.width)

    def add_block(self):
        self.length += 1
        self.body.append(Block(self.body[-1].px, self.body[-1].py))

    def move(self):
        self.head.move()
        for i in range(1, self.length):
            self.body[i].follow(self.body[i - 1])

    def collide(self):
        x, y = self.head.get_block()
        if x > 499 or x < 0 or y > 499 or y < 0:
            return True
        for i in range(1, self.length):
            if self.head.get_block() == self.body[i].get_block():
                return True
        return False

    def spawn_food(self, window):
        good_spawn=True
        while good_spawn:
            for i in range(1, self.length):
                if (self.food.x, self.food.y) == self.body[i].get_block():
                    self.food = Food(self.head.width * 25, self.head.width)
                    good_spawn = False
            if good_spawn:
                break
        self.food.spawn(window)
        if self.food.eaten(self):
            self.add_block()

    def print_snake(self, window):
        for i in self.body:
            i.show_me(window)


class Food:
    def __init__(self, dimensions, step):
        self.size = step
        self.dimensions = dimensions
        self.x = random.randrange(0, dimensions, step)
        self.y = random.randrange(0, dimensions, step)

    def spawn(self, window):
        pygame.draw.rect(window, (150, 150, 150), (self.x, self.y, self.size, self.size))

    def eaten(self, head):
        if head.head.get_block() == (self.x, self.y):
            self.x = random.randrange(0, self.dimensions, self.size)
            self.y = random.randrange(0, self.dimensions, self.size)
            return True
        return False


if __name__ == "__main__":
    pygame.init()
    mygame = pygame.display.set_mode((500, 500))
    pygame.display.set_caption("Snake")
    run = True
    player = Snake(20, 20)

    while run:

        pygame.time.delay(60)
        input = pygame.key.get_pressed()

        for event in pygame.event.get():
            if event.type == pygame.QUIT or input[pygame.K_ESCAPE]:
                run = False

        mygame.fill((0, 0, 0))
        pygame.draw.line(mygame, (255, 255, 255), (0, 0), (0, 499))
        pygame.draw.line(mygame, (255, 255, 255), (0, 0), (499, 0))
        pygame.draw.line(mygame, (255, 255, 255), (0, 499), (499, 499))
        pygame.draw.line(mygame, (255, 255, 255), (499, 0), (499, 499))

        player.print_snake(mygame)

        if player.head.direction == "U" or player.head.direction == "D":
            if input[pygame.K_LEFT]:
                player.head.change_direction("L")
            if input[pygame.K_RIGHT]:
                player.head.change_direction("R")
        if player.head.direction == "L" or player.head.direction == "R":
            if input[pygame.K_UP]:
                player.head.change_direction("U")
            if input[pygame.K_DOWN]:
                player.head.change_direction("D")

        player.move()
        if player.collide():
            run = False

        player.spawn_food(mygame)
        pygame.display.update()

    pygame.quit()
