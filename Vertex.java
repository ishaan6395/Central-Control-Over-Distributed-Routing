/**
 * Vertex.java
 */

/**
 * Program having all the attributes and methods of a Vertex of a Graph
 * @author Ishaan Thakker
 * @author Amol Gaikwad
 * @author Neel Desai
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
class Vertex implements Comparable<Vertex> {
	
	public String id, correspondsto;
	public Integer distance;
	
	public Vertex(String id, String correspondsto, Integer distance) {
		super();
		this.id = id;
		this.distance = distance;
		this.correspondsto = correspondsto;
	}

	public String getCorrespondsto(){
		return correspondsto;
	}

	public void setCorrespondsto(String correspondsto){
		this.correspondsto = correspondsto;
	}

	public String getId() {
		return id;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vertex [id=" + id + ", distance=" + distance + "]";
	}

	@Override
	public int compareTo(Vertex o) {
		if (this.distance < o.distance)
			return -1;
		else if (this.distance > o.distance)
			return 1;
		else
			return this.getId().compareTo(o.getId());
	}
	
}

