package searchmap

import (
	"bytes"
	"errors"
	"fmt"
	"log"
	"os"
	"strings"
)

const (
	_ int8 = iota
	NORTH
	SOUTH
	EAST
	WEST
)

type MapPosition struct {
	x int
	y int
}

type PathMap struct {
	mapString       []string
	path            []int8
	currentPosition MapPosition
}

func SearchMapFromFile(filename *string) error {
	pm, err := readInMap(filename)
	if err != nil {
		log.Fatal(err)
		return err
	}

	return SearchMap(&pm)
}

func SearchMap(pm *PathMap) error {
	frontier := []PathMap{*pm}
	// keep track of all visited positions to avoid evaluating the same
	// spot twice
	visited := make([][]bool, len(pm.mapString))
	for i := range visited {
		visited[i] = make([]bool, len(pm.mapString[0]))
	}
	visited[pm.currentPosition.y][pm.currentPosition.x] = true

	goal, err := pm.getGoalPosition()
	if err != nil {
		return err
	}

	for len(frontier) > 0 {
		// pop off the top item from the frontier
		curr := frontier[0]
		frontier = frontier[1:]

		if curr.currentPosition.x == goal.x && curr.currentPosition.y == goal.y {
			fmt.Println("Found a path!")
			fmt.Printf("Goal: x: %d, y: %d\n", goal.x, goal.y)
			fmt.Printf("Current Pos: x: %d, y: %d\n", curr.currentPosition.x, curr.currentPosition.x)
			fmt.Println(frontier[0].string())
			return nil
		}

		frontier = append(frontier, curr.expandMap(visited)...)
	}

	fmt.Println("Could not find a path")

	return nil
}

func (pm *PathMap) expandMap(visited [][]bool) []PathMap {
	maps := []PathMap{}
	x, y := pm.currentPosition.x, pm.currentPosition.y

	if y-1 >= 0 && !visited[y-1][x] && pm.isValidExpassion(x, y-1) {
		newNorth := PathMap{
			mapString:       pm.mapString,
			path:            make([]int8, len(pm.path)),
			currentPosition: MapPosition{x, y - 1},
		}
		copy(newNorth.path, pm.path)
		newNorth.path = append(newNorth.path, NORTH)
		maps = append(maps, newNorth)
		visited[x][y-1] = true
	}
	if y+1 < len(visited) && !visited[y+1][x] && pm.isValidExpassion(x, y+1) {
		newSouth := PathMap{
			mapString:       pm.mapString,
			path:            make([]int8, len(pm.path)),
			currentPosition: MapPosition{x, y + 1},
		}
		copy(newSouth.path, pm.path)
		newSouth.path = append(newSouth.path, SOUTH)
		maps = append(maps, newSouth)
		visited[y+1][x] = true
	}
	if x-1 >= 0 && !visited[y][x-1] && pm.isValidExpassion(x-1, y) {
		newWest := PathMap{
			mapString:       pm.mapString,
			path:            make([]int8, len(pm.path)),
			currentPosition: MapPosition{x - 1, y},
		}
		copy(newWest.path, pm.path)
		newWest.path = append(newWest.path, WEST)
		maps = append(maps, newWest)
		visited[y][x-1] = true
	}
	if x+1 < len(visited[y]) && !visited[y][x+1] && pm.isValidExpassion(x+1, y) {
		newEast := PathMap{
			mapString:       pm.mapString,
			path:            make([]int8, len(pm.path)),
			currentPosition: MapPosition{x + 1, y},
		}
		copy(newEast.path, pm.path)
		newEast.path = append(newEast.path, EAST)
		maps = append(maps, newEast)
		visited[y][x+1] = true
	}

	return maps
}

func readInMap(fileName *string) (PathMap, error) {
	file, err := os.ReadFile(*fileName)
	if err != nil {
		return PathMap{}, err
	}

	ms := strings.Split(string(file), "\n")
	p := []int8{}

	pm := PathMap{mapString: ms, path: p}
	pm.setCurrentPosition()

	return pm, nil
}

func (pm *PathMap) isValidExpassion(x int, y int) bool {
	// check position in bounds
	if y < 0 || y > len(pm.mapString) || x < 0 || x > len(pm.mapString[y]) {
		return false
	}

	// check position is not current position
	if pm.currentPosition.x == x && pm.currentPosition.y == y {
		return false
	}

	// check position is a valid character
	return pm.mapString[y][x] == ' ' || pm.mapString[y][x] == 'G'
}

func (pm *PathMap) setCurrentPosition() {
	pos, err := pm.getStartPosition()
	pm.currentPosition = pos
	if err != nil {
		log.Printf("Could not find start position, error: %s\n", err.Error())
		return
	}

	for _, p := range pm.path {
		switch p {
		case NORTH:
			pm.currentPosition.y -= 1
		case SOUTH:
			pm.currentPosition.y += 1
		case WEST:
			pm.currentPosition.x -= 1
		case EAST:
			pm.currentPosition.x += 1
		}
	}
}

func (pm *PathMap) getGoalPosition() (MapPosition, error) {
	for x, s := range pm.mapString {
		y := strings.Index(s, "G")
		if y != -1 {
			return MapPosition{x, y}, nil
		}
	}

	return MapPosition{-1, -1}, errors.New("could not find goal")
}

func (pm *PathMap) getStartPosition() (MapPosition, error) {
	for y, s := range pm.mapString {
		x := strings.Index(s, "S")
		if x != -1 {
			return MapPosition{x, y}, nil
		}
	}
	return MapPosition{-1, -1}, errors.New("could not find start")
}

func (pm *PathMap) drawPath() ([]string, error) {
	// need to convert to a rune, as strings are immutable
	mapString := make([][]rune, len(pm.mapString))
	for i, s := range (pm).mapString {
		mapString[i] = []rune(s)
	}

	pos, err := pm.getStartPosition()
	if err != nil {
		log.Panic(err)
		return nil, err
	}

	dot := rune('.')
	for _, d := range (pm).path {
		switch d {
		case NORTH:
			pos.y -= 1
		case SOUTH:
			pos.y += 1
		case EAST:
			pos.x += 1
		case WEST:
			pos.x -= 1
		}
		mapString[pos.y][pos.x] = dot
	}

	// convert back to string to return
	res := make([]string, len(mapString))
	for i, s := range mapString {
		res[i] = string(s)
	}
	return res, nil
}

func (pm *PathMap) string() string {
	var out bytes.Buffer

	// draw path onto map

	out.WriteString(strings.Join(pm.mapString, "\n"))

	out.WriteString("\nPath: [")
	for _, p := range pm.path {
		switch p {
		case NORTH:
			out.WriteString("North,")
		case SOUTH:
			out.WriteString("South,")
		case EAST:
			out.WriteString("East,")
		case WEST:
			out.WriteString("West,")
		}
	}
	out.WriteString("]")

	return out.String()
}
