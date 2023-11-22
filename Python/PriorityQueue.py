from typing import TypeVar, Generic

T = TypeVar('T')

class PriorityQueue(Generic[T]):
  def __init__(self) -> None:
    self.queue: list[list[T | float]] = []

  def __str__(self) -> str:
    return str(self.queue)
  
  def peek(self) -> T:
    return self.queue[0][0]

  def pop(self) -> T:
    return self.queue.pop()[0]
  
  def insert(self, item: T, priority: float) -> None:
    i = 0
    while i != len(self.queue) and self.queue[i][1] > priority:
      i += 1
    self.queue.insert(i,[item,priority])