package searchmap

import (
	"bytes"
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

func SearchMap(filename *string) {
	pm, err := readInMap(filename)
	if err != nil {
		log.Fatal(err)
		return
	}

	frontier := []PathMap{pm}
	// using string because I am unsure if []string or the PathMap struct are hashing address or value,
	// since they both require a pointer to the data and I do not know if this will result in duplicate
	// values being added to the map
	visted := make(map[string]bool)

	for len(frontier) > 0 {
		curr := frontier[0]
		frontier = frontier[1:]

		flatMapString := strings.Join(curr.mapString, "")
		visted[flatMapString] = true

		frontier = append(frontier, curr.expandMap(visted)...)
		break
	}

	fmt.Println("DONE!")
}

func (pm *PathMap) expandMap(visited map[string]bool) []PathMap {
	maps := []PathMap{}
	x, y := pm.currentPosition[0], pm.currentPosition[1]

	if x-1 > 0 && !visited[fmt.Sprintf("%d,%d", x-1, y)] && pm.isValidExpansion(x-1, y) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, NORTH),
			currentPosition: []int{x - 1, y},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x-1, y)] = true
		fmt.Println("NORTH")
	}
	if x+1 < len(pm.mapString)-1 && !visited[fmt.Sprintf("%d,%d", x+1, y)] && pm.isValidExpansion(x+1, y) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, SOUTH),
			currentPosition: []int{x + 1, y},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x+1, y)] = true
		fmt.Println("SOUTH")
	}
	if y-1 > 0 && !visited[fmt.Sprintf("%d,%d", x, y-1)] && pm.isValidExpansion(x, y-1) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, WEST),
			currentPosition: []int{x, y - 1},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x, y-1)] = true
		fmt.Println("WEST")
	}
	if y+1 < len(pm.mapString[x])-1 && !visited[fmt.Sprintf("%d,%d", x, y+1)] && pm.isValidExpansion(x, y+1) {
		nm := PathMap{
			mapString:       pm.mapString,
			path:            append(pm.path, EAST),
			currentPosition: []int{x, y + 1},
		}
		maps = append(maps, nm)
		visited[fmt.Sprintf("%d,%d", x, y+1)] = true
		fmt.Println("EAST")
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

func (pm *PathMap) isValidExpansion(x int, y int) bool {
	if pm.currentPosition[0] == x && pm.currentPosition[1] == y {
		return false
	}

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

func (pm *PathMap) String() string {
	var out bytes.Buffer

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
