/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */


package net.imglib2.ops.pointset;


import java.util.Arrays;

import net.imglib2.ops.PointSet;
import net.imglib2.ops.PointSetIterator;
import net.imglib2.ops.Tuple2;
import net.imglib2.ops.parse.PointSetParser;

/**
 * Class for defining complex PointSets from a text string. Syntax is similar
 * to list comprehension syntax in Haskell.
 * 
 * @author Barry DeZonia
 *
 */
public class TextSpecifiedPointSet implements PointSet {

	private PointSet set;
	private String origInput;
	private String error;
	
	public TextSpecifiedPointSet(String specification) {
		origInput = specification;
		error = null;
		set = construct(specification);
	}
	
	@Override
	public long[] getAnchor() {
		return set.getAnchor();
	}

	@Override
	public void setAnchor(long[] anchor) {
		set.setAnchor(anchor);
	}

	@Override
	public PointSetIterator createIterator() {
		return set.createIterator();
	}

	@Override
	public int numDimensions() {
		return set.numDimensions();
	}

	@Override
	public long[] findBoundMin() {
		return set.findBoundMin();
	}

	@Override
	public long[] findBoundMax() {
		return set.findBoundMax();
	}

	@Override
	public boolean includes(long[] point) {
		return set.includes(point);
	}

	@Override
	public long calcSize() {
		return set.calcSize();
	}

	@Override
	public TextSpecifiedPointSet copy() {
		return new TextSpecifiedPointSet(origInput);
	}

	public String getErrorString() {
		return error;
	}
	
	private PointSet construct(String spec) {
		PointSetParser parser = new PointSetParser();
		Tuple2<PointSet,String> results = parser.parse(spec);
		if (results.get2() != null) {
			error = results.get2();
			return new EmptyPointSet();
		}
		return results.get1();
	}

	public static void main(String[] args) {
		TextSpecifiedPointSet ps;
		PointSetIterator iter;

		/*
		// test all tokens
		String allTokens =
				" var = [ ] 01234 , 56789.12 .. mod < > <= >= == != of + - / * " +
						" within ( ) ^ E PI ||";
		HashMap<String, Integer> varMap = new HashMap<String, Integer>();
		ps = new TextSpecifiedPointSet("");
		List<SpecToken> tokens = ps.tokenize(allTokens, varMap);
		System.out.println("last token should be OR and is "+
				tokens.get(tokens.size()-1).getClass());
		*/
		
		// test some definitions
		//new TextSpecifiedPointSet("");
		ps = new TextSpecifiedPointSet("x = [5]");
		System.out.println("result = " +
				(ps.getErrorString() == null ? "OK" : ps.getErrorString()));
		ps = new TextSpecifiedPointSet("x = [5], y=[1..10]");
		System.out.println("result = " +
				(ps.getErrorString() == null ? "OK" : ps.getErrorString()));
		ps = new TextSpecifiedPointSet("x= [1..2], y=[ 1, 3 .. 10 ]");
		System.out.println("result = " +
				(ps.getErrorString() == null ? "OK" : ps.getErrorString()));
		ps = new TextSpecifiedPointSet("xx =[1,4..12] ,yy = [1..5] , xx + yy <= 12");
		System.out.println("result = " +
				(ps.getErrorString() == null ? "OK" : ps.getErrorString()));
		ps = new TextSpecifiedPointSet("x = [1,7,9], y=[1,4,5]");
		System.out.println("result = " +
				(ps.getErrorString() == null ? "OK" : ps.getErrorString()));
		
		// iterate some definitions
		System.out.println("Iterate x = [5]");
		ps = new TextSpecifiedPointSet("x = [5]");
		iter = ps.createIterator();
		while (iter.hasNext()) {
			long[] next = iter.next();
			System.out.println(" " + Arrays.toString(next));
		}
		
		System.out.println("Iterate x = [1..3], y = [3..5]");
		ps = new TextSpecifiedPointSet("x = [1..3], y = [3..5]");
		iter = ps.createIterator();
		while (iter.hasNext()) {
			long[] next = iter.next();
			System.out.println(" " + Arrays.toString(next));
		}

		System.out.println("Iterate x = [1,3..7], y = [5,8..14]");
		ps = new TextSpecifiedPointSet("x = [1,3..7], y = [5,8..14]");
		iter = ps.createIterator();
		while (iter.hasNext()) {
			long[] next = iter.next();
			System.out.println(" " + Arrays.toString(next));
		}

		System.out.println("Iterate x = [1,4,9], y = [2,3,5,8]");
		ps = new TextSpecifiedPointSet("x = [1,4,9], y = [2,3,5,8]");
		iter = ps.createIterator();
		while (iter.hasNext()) {
			long[] next = iter.next();
			System.out.println(" " + Arrays.toString(next));
		}

		System.out.println("Iterate x = [-2..2], y = [-1,1..5]");
		ps = new TextSpecifiedPointSet("x = [-2..2], y = [-1,1..5]");
		iter = ps.createIterator();
		while (iter.hasNext()) {
			long[] next = iter.next();
			System.out.println(" " + Arrays.toString(next));
		}
		
		System.out.println("Iterate x=[1..5],y=[1,5..50]");
		ps = new TextSpecifiedPointSet("x=[1..5],y=[1,5..50]");
		iter = ps.createIterator();
		while (iter.hasNext()) {
			long[] next = iter.next();
			System.out.println(" " + Arrays.toString(next));
		}

		System.out.println("Iterate x=[1..20],y=[1..20],x+y < 7");
		ps = new TextSpecifiedPointSet("x=[1..20],y=[1..20],x+y < 7");
		if (ps.getErrorString() != null)
			System.out.println(ps.getErrorString());
		else {
			iter = ps.createIterator();
			while (iter.hasNext()) {
				long[] next = iter.next();
				System.out.println(" " + Arrays.toString(next));
			}
		}

		System.out.println("Iterate x=[-5..5],y=[-5..5],x^2+y^2 <= 3");
		ps = new TextSpecifiedPointSet("x=[-5..5],y=[-5..5],x^2+y^2 <= 3");
		if (ps.getErrorString() != null)
			System.out.println(ps.getErrorString());
		else {
			iter = ps.createIterator();
			while (iter.hasNext()) {
				long[] next = iter.next();
				System.out.println(" " + Arrays.toString(next));
			}
		}
	}
}