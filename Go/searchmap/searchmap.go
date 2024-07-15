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

type PathMap struct {
	mapString       []string
	path            []int8
	currentPosition []int // [x, y]
}

func SearchMap(filename *string) error {
	pm, err := readInMap(filename)
	if err != nil {
		log.Fatal(err)
		return err
	}

	frontier := []PathMap{pm}
	// using string because I am unsure if []string or the PathMap struct are
	// hashing address or value, since they both require a pointer to the data
	// and I do not know if this will result in duplicate values being added to the map
	visted := make(map[string]bool)
	goal, err := pm.getGoalPosition()
	if err != nil {
		return err
	}

	for len(frontier) > 0 {
		curr := frontier[0]
		frontier = frontier[1:]
		if curr.currentPosition[0] == goal[0] && curr.currentPosition[1] == goal[1] {
			fmt.Println("Found a path!")
			fmt.Println(frontier[0].string())
			return nil
		}

		flatMapString := strings.Join(curr.mapString, "")
		visted[flatMapString] = true

		frontier = append(frontier, curr.expandMap(visted)...)
	}

	fmt.Println("Could not find a path")

	return nil
}

func (pm *PathMap) expandMap(visited map[string]bool) []PathMap {
	maps := []PathMap{}
	x, y := pm.currentPosition[0], pm.currentPosition[1]

	if !visited[fmt.Sprintf("%d,%d", x-1, y)] && pm.isValidExpassion(x-1, y) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, NORTH),
			currentPosition: []int{x - 1, y},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x-1, y)] = true
	}
	if !visited[fmt.Sprintf("%d,%d", x+1, y)] && pm.isValidExpassion(x+1, y) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, SOUTH),
			currentPosition: []int{x + 1, y},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x+1, y)] = true
	}
	if !visited[fmt.Sprintf("%d,%d", x, y-1)] && pm.isValidExpassion(x, y-1) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, WEST),
			currentPosition: []int{x, y - 1},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x, y-1)] = true
	}
	if !visited[fmt.Sprintf("%d,%d", x, y+1)] && pm.isValidExpassion(x, y+1) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, EAST),
			currentPosition: []int{x, y + 1},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x, y+1)] = true
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
	if x < 0 || x > len(pm.mapString) || y < 0 || y > len(pm.mapString[x]) {
		return false
	}

	// check position is not current position
	if pm.currentPosition[0] == x && pm.currentPosition[1] == y {
		return false
	}

	// check position is a valid character
	return pm.mapString[x][y] == ' ' || pm.mapString[x][y] == 'G'
}

func (pm *PathMap) setCurrentPosition() {
	for x, s := range pm.mapString {
		y := strings.Index(s, "S")
		if y != -1 {
			pm.currentPosition = []int{x, y}
			break
		}
	}

	if pm.currentPosition == nil {
		pm.currentPosition = []int{-1, -1}
	}

	for _, p := range pm.path {
		switch p {
		case NORTH:
			pm.currentPosition[0] -= 1
		case SOUTH:
			pm.currentPosition[0] += 1
		case WEST:
			pm.currentPosition[1] -= 1
		case EAST:
			pm.currentPosition[1] += 1
		}
	}
}

func (pm *PathMap) getGoalPosition() ([]int, error) {
	for x, s := range pm.mapString {
		y := strings.Index(s, "G")
		if y != -1 {
			return []int{x, y}, nil
		}
	}

	return nil, errors.New("could not find goal")
}

func (pm *PathMap) getStartPosition() ([]int, error) {
	for x, s := range pm.mapString {
		y := strings.Index(s, "S")
		if y != -1 {
			return []int{x, y}, nil
		}
	}
	return nil, errors.New("could not find start")
}

func (pm *PathMap) drawPath() ([]string, error) {
	mapString := (pm).mapString

	pos, err := pm.getStartPosition()
	if err != nil {
		log.Panic(err)
		return nil, err
	}

		
}

func (pm *PathMap) string() string {
	var out bytes.Buffer

	// draw path onto map

	out.WriteString(strings.Join(pm.mapString, "\n"))

	out.WriteString("Path: [")
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
