package searchmap

import (
	"fmt"
	"testing"
)

func TestMapExpand(t *testing.T) {
	ms := []string{
		"+--------------------+",
		"|                    |",
		"|                    |",
		"|            GX      |",
		"|   X       XXX      |",
		"|  XXXX   XXXXX      |",
		"| XXXX    X  XX      |",
		"|   SX               |",
		"|                    |",
		"+--------------------+",
	}

	pm := PathMap{
		mapString: ms,
		path:      []int8{},
	}
	pm.setCurrentPosition()
	fmt.Printf("x: %d,y: %d\n", pm.currentPosition[0], pm.currentPosition[1])

	visited := map[string]bool{}

	// em := pm.expandMap(visited)

	// if len(em) != 2 {
	// 	t.Fatalf("expected expanded maps to have a length of 2. got=%d", len(em))
	// }

	// if em[0].path[0] == EAST || em[0].path[0] == NORTH {
	// 	s := "NORTH"
	// 	if em[0].path[0] == EAST {
	// 		s = "EAST"
	// 	}
	// 	t.Fatalf("expected west or south direction in path. got=%s", s)
	// }

	// if em[1].path[0] == EAST || em[1].path[0] == NORTH {
	// 	s := "NORTH"
	// 	if em[0].path[0] == EAST {
	// 		s = "EAST"
	// 	}
	// 	t.Fatalf("expected west or south direction in path. got=%s", s)
	// }

	pm.path = append(pm.path, SOUTH, EAST, EAST)

	x, y := pm.currentPosition[0], pm.currentPosition[1]
	visited[fmt.Sprintf("%d,%d", x, y)] = true
	visited[fmt.Sprintf("%d,%d", x+1, y)] = true
	visited[fmt.Sprintf("%d,%d", x+1, y+1)] = true
	visited[fmt.Sprintf("%d,%d", x+1, y+2)] = true
	pm.setCurrentPosition()

	em := pm.expandMap(visited)
	fmt.Printf("x: %d,y: %d\n", pm.currentPosition[0], pm.currentPosition[1])

	if len(em) != 3 {
		t.Fatalf("expected expanded maps to have a length of 3. got=%d", len(em))
	}
}

func TestSetPosition(t *testing.T) {
	ms := []string{
		"+--------------------+",
		"|                    |",
		"|                    |",
		"|            GX      |",
		"|   X       XXX      |",
		"|  XXXX   XXXXX      |",
		"| XXXX    X  XX      |",
		"|   SX               |",
		"|                    |",
		"+--------------------+",
	}

	pm := PathMap{
		mapString: ms,
		path:      []int8{},
	}
	pm.setCurrentPosition()

	if pm.currentPosition[0] != 6 && pm.currentPosition[1] != 4 {
		t.Fatalf("expected current position to be [6,4]. got=%+v", pm.currentPosition)
	}

	pm.path = append(pm.path, SOUTH, EAST, EAST, EAST, NORTH, WEST)
	pm.setCurrentPosition()

	if pm.currentPosition[0] != 6 && pm.currentPosition[1] != 6 {
		t.Fatalf("expected current position to be [6,6]. got=%+v", pm.currentPosition)
	}
}
