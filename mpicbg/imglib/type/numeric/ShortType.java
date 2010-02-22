/**
 * Copyright (c) 2009--2010, Stephan Preibisch & Stephan Saalfeld
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
 *
 * @author Stephan Preibisch & Stephan Saalfeld
 */
package mpicbg.imglib.type.numeric;

import mpicbg.imglib.container.Container;
import mpicbg.imglib.container.basictypecontainer.ShortContainer;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.image.display.ShortTypeDisplay;

public class ShortType extends GenericShortType<ShortType>
{
	// this is the constructor if you want it to read from an array
	public ShortType( final ShortContainer<ShortType> shortStorage ) { super( shortStorage ); }
	
	// this is the constructor if you want it to be a variable
	public ShortType( final short value ) { super( value ); }

	// this is the constructor if you want it to be a variable
	public ShortType() { this( (short)0 ); }
	
	public short get() { return v[ i ]; }
	public void set( final short b ) { v[ i ] = b; }

	@Override
	public ShortTypeDisplay getDefaultDisplay( Image<ShortType> image ) { return new ShortTypeDisplay( image );	}

	@Override
	public ShortType[] createArray1D( final int size1 ){ return new ShortType[ size1 ]; }

	@Override
	public ShortType[][] createArray2D( final int size1, final int size2 ){ return new ShortType[ size1 ][ size2 ]; }

	@Override
	public ShortType[][][] createArray3D( final int size1, final int size2, final int size3 ) { return new ShortType[ size1 ][ size2 ][ size3 ]; }
	
	@Override
	public ShortType createType( final Container<ShortType> container )
	{
		return new ShortType( (ShortContainer<ShortType>)container );
	}

	@Override
	public ShortType createVariable(){ return new ShortType( (short)0 ); }

	@Override
	public ShortType clone(){ return new ShortType( v[ i ] ); }
}