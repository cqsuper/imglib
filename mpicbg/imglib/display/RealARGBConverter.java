/**
 * Copyright (c) 2009--2011, Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package mpicbg.imglib.display;

import mpicbg.imglib.converter.Converter;
import mpicbg.imglib.type.numeric.ARGBType;
import mpicbg.imglib.type.numeric.real.FloatType;

/**
 * 
 *
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 */
public class RealARGBConverter implements Converter< FloatType, ARGBType >
{
	protected double min = 0;
	protected double max = 1;
	protected double scale = 1;
	
	public RealARGBConverter() {}
	
	public RealARGBConverter( final int min, final int max )
	{
		this.min = min;
		this.max = max;
		scale = max - min;
	}
	
	final static protected int roundPositive( final double a )
	{
		return ( int )( a + 0.5 );
	}
	
	@Override
	public void convert( final FloatType input, final ARGBType output )
	{
		final double a = input.getRealDouble();
		final int b = Math.min( 255, roundPositive( Math.max( 0, ( ( a - min ) / scale * 255.0 ) ) ) );
		final int argb = ( ( ( b << 8 ) | b ) << 8 ) | b;
		output.set( argb );
	}
	
	
}