{
	"format_version": "1.12.0",
	"minecraft:geometry": [
		{
			"description": {
				"identifier": "geometry.sandworm",
				"texture_width": 16,
				"texture_height": 16,
				"visible_bounds_width": 2,
				"visible_bounds_height": 1.5,
				"visible_bounds_offset": [0, 0.25, 0]
			},
			"bones": [
				{
					"name": "worm",
					"pivot": [0, 0, 0]
				},
				{
					"name": "head",
					"parent": "worm",
					"pivot": [-0.5, 1.5, -6],
					"cubes": [
						{"origin": [-2, 0, -7], "size": [3, 3, 2], "uv": [0, 5]}
					]
				},
				{
					"name": "part1",
					"parent": "worm",
					"pivot": [-0.5, 1.5, -3.5],
					"cubes": [
						{"origin": [-2, 0, -5], "size": [3, 3, 3], "uv": [0, 0]}
					]
				},
				{
					"name": "part2",
					"parent": "worm",
					"pivot": [-0.5, 1.5, -0.5],
					"cubes": [
						{"origin": [-2, 0, -2], "size": [3, 3, 3], "uv": [0, 0]}
					]
				},
				{
					"name": "part3",
					"parent": "worm",
					"pivot": [-0.5, 1.5, 2.5],
					"cubes": [
						{"origin": [-2, 0, 1], "size": [3, 3, 3], "uv": [0, 0]}
					]
				},
				{
					"name": "part4",
					"parent": "worm",
					"pivot": [-0.5, 1.5, 5.5],
					"cubes": [
						{"origin": [-2, 0, 4], "size": [3, 3, 3], "uv": [0, 0]}
					]
				}
			]
		}
	]
}