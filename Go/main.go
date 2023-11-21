package main

import "pathmap/searchmap"

func main() {
	f := "../Maps/map.txt"
	searchmap.SearchMap(&f)
}
