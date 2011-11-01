package gtamapedit.file;

import gtamapedit.conf.Configuration;

public class OBJBuilder {
	public static String buildObjFromMap(MapFileTileElement[][] mapData){
		StringBuilder v = new StringBuilder();
		StringBuilder vn = new StringBuilder();
		StringBuilder f = new StringBuilder();
		int halfts = Configuration.tileSize/2;
		int ts = Configuration.tileSize/2;
		int currentIndex = 0; 
		int vi = 0; //vertexindex
		
		//west
		vn.append("vn -1.000000 0.000000 0.000000\n");
		//east
		vn.append("vn 1.000000 0.000000 0.000000\n");
		//up
		vn.append("vn 0.000000 0.000000 1.000000\n");
		//south
		vn.append("vn -0.000000 1.000000 -0.000000\n");
		//north
		vn.append("vn 0.000000 -1.000000 0.000000\n");
		
		
		for(int x=0; x<mapData.length; x++){
			for(int y=0; y<mapData[0].length; y++){
				
				//floor
				int fl = mapData[x][y].getFloors();
				v.append(
						"v "+(ts*x)+" "+(ts*y)+" "+(ts*fl)+"\n"
				);
				v.append(
						"v "+(ts*x)+" "+(ts+ts*y)+" "+(ts*fl)+"\n"
				);
				v.append(
						"v "+(ts+ts*x)+" "+(ts+ts*y)+" "+(ts*fl)+"\n"
				);
				v.append(
						"v "+(ts+ts*x)+" "+(ts*y)+" "+(ts*fl)+"\n"
				);
				
				f.append("f "+(vi+1)+"//3 "+(vi+2)+"//3 "+(vi+3)+"//3"+"\n");
				f.append("f "+(vi+1)+"//3 "+(vi+3)+"//3 "+(vi+4)+"//3"+"\n");
				vi += 4;
				
				for(fl=0; fl<mapData[x][y].getFloors(); fl++){
					//DrawWalls
					//TODO remove unnecessary
					
					
					//southwall
					
						v.append(
								"v "+(ts*x)+" "+(ts+ts*y)+" "+(ts+ts*fl)+"\n"
						);
						v.append(
								"v "+(ts*x)+" "+(ts+ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts+ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts+ts*y)+" "+(ts+ts*fl)+"\n"
						);
						
						f.append("f "+(vi+1)+"//4 "+(vi+2)+"//4 "+(vi+3)+"//4"+"\n");
						f.append("f "+(vi+1)+"//4 "+(vi+3)+"//4 "+(vi+4)+"//4"+"\n");
						vi += 4;
					
					
					//eastwall
						
						v.append(
								"v "+(ts+ts*x)+" "+(ts+ts*y)+" "+(ts+ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts+ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts*y)+" "+(ts+ts*fl)+"\n"
						);
						
						f.append("f "+(vi+1)+"//2 "+(vi+2)+"//2 "+(vi+3)+"//2"+"\n");
						f.append("f "+(vi+1)+"//2 "+(vi+3)+"//2 "+(vi+4)+"//2"+"\n");
						vi += 4;
					
					//northwall
						
						v.append(
								"v "+(ts*x)+" "+(ts*y)+" "+(ts+ts*fl)+"\n"
						);
						v.append(
								"v "+(ts*x)+" "+(ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts+ts*x)+" "+(ts*y)+" "+(ts+ts*fl)+"\n"
						);
						
						f.append("f "+(vi+1)+"//5 "+(vi+2)+"//5 "+(vi+3)+"//5"+"\n");
						f.append("f "+(vi+1)+"//5 "+(vi+3)+"//5 "+(vi+4)+"//5"+"\n");
						vi += 4;
					
					//westwall
						v.append(
								"v "+(ts*x)+" "+(ts+ts*y)+" "+(ts+ts*fl)+"\n"
						);
						v.append(
								"v "+(ts*x)+" "+(ts+ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts*x)+" "+(ts*y)+" "+(ts*fl)+"\n"
						);
						v.append(
								"v "+(ts*x)+" "+(ts*y)+" "+(ts+ts*fl)+"\n"
						);
						
						f.append("f "+(vi+1)+"//1 "+(vi+2)+"//1 "+(vi+3)+"//1"+"\n");
						f.append("f "+(vi+1)+"//1 "+(vi+3)+"//1 "+(vi+4)+"//1"+"\n");
						vi += 4;
					
				}
			}	
		}
		
		return v.toString()+vn.toString()+f.toString();
		
	}


	/**
	v -1.000000 1.000000 1.000000
	v -1.000000 1.000000 -1.000000
	v -1.000000 -1.000000 -1.000000
	v 1.000000 -0.999999 1.000000
	v 1.000000 1.000001 0.999999
	v 1.000000 1.000000 -1.000000
	v 1.000000 -1.000000 -1.000000
	vn -1.000000 0.000000 0.000000
	vn 1.000000 0.000000 0.000000
	vn 0.000000 0.000000 1.000000
	vn -0.000000 1.000000 -0.000000
	vn -0.000000 -0.000000 -1.000000
	vn 0.000000 -1.000000 0.000000
	s off
	f 1//1 2//1 3//1
	f 1//1 3//1 4//1
	
	f 5//2 8//2 7//2
	f 5//2 7//2 6//2
	f 1//3 5//3 6//3
	f 1//3 6//3 2//3
	f 2//4 6//4 7//4
	f 2//4 7//4 3//4
	f 3//5 7//5 8//5
	f 3//5 8//5 4//5
	f 5//6 1//6 4//6
	f 5//6 4//6 8//6
	**/

}
