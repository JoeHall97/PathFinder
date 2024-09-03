package searchmap

import (
	"fmt"
	"testing"
)

func TestMapSearch(t *testing.T) {
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

	err := SearchMap(&pm)
	if err != nil {
		t.Fatal(err)
	}
}

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
	fmt.Printf("x: %d,y: %d\n", pm.currentPosition.x, pm.currentPosition.y)

	visited := make([][]bool, len(ms))
	for i := range ms {
		visited[i] = make([]bool, len(ms[i]))
	}

	em := pm.expandMap(visited)

	if len(em) != 2 {
		t.Fatalf("expected expanded maps to have a length of 2. got=%d", len(em))
	}

	if em[0].path[0] == EAST || em[0].path[0] == NORTH {
		s := "NORTH"
		if em[0].path[0] == EAST {
			s = "EAST"
		}
		t.Fatalf("expected west or south direction in path. got=%s", s)
	}

	north, south, east, west := false, false, false, false
	for _, m := range em {
		switch m.path[0] {
		case NORTH:
			t.Fatalf("expanded north when it should not have")
		case SOUTH:
			if south {
				t.Fatalf("expanded south position twice")
			}
			south = true
		case EAST:
			t.Fatalf("expanded east when it should not have")
		case WEST:
			if west {
				t.Fatalf("expanded west twice")
			}
			west = true
		}
	}

	pm.path = append(pm.path, SOUTH, EAST, EAST, EAST)

	x, y := pm.currentPosition.x, pm.currentPosition.y
	visited[y][x] = true
	visited[y+1][y] = true
	visited[y+1][x+1] = true
	visited[y+1][x+2] = true
	visited[y+1][x+3] = true
	pm.setCurrentPosition()

	maps, _ := pm.drawPath()
	for _, m := range maps {
		fmt.Println(m)
	}
	em = pm.expandMap(visited)

	if len(em) != 2 {
		t.Fatalf("expected expanded maps to have a length of 3. got=%d", len(em))
	}

	north, south, east, west = false, false, false, false
	for _, m := range em {
		switch m.path[4] {
		case NORTH:
			if north {
				t.Fatalf("expanded north position twice")
			}
			north = true
		case SOUTH:
			if south {
				t.Fatalf("expanded south position twice")
			}
			south = true
		case EAST:
			if east {
				t.Fatalf("expanded east position twice")
			}
			east = true
		case WEST:
			t.Fatalf("expanded west when it should not have")
		}
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

	fmt.Printf("%s\n", string(pm.mapString[pm.currentPosition.y][pm.currentPosition.x]))

	if pm.currentPosition.y != 7 && pm.currentPosition.x != 4 {
		t.Fatalf("expected current position to be [6,4]. got=%+v", pm.currentPosition)
	}

	pm.path = append(pm.path, SOUTH, EAST, EAST, EAST, NORTH, WEST)
	pm.setCurrentPosition()

	if pm.currentPosition.y != 7 && pm.currentPosition.x != 6 {
		t.Fatalf("expected current position to be [6,6]. got=%+v", pm.currentPosition)
	}
}
