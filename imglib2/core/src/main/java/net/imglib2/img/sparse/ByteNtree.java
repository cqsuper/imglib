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

package net.imglib2.img.sparse;

import net.imglib2.img.basictypeaccess.ByteAccess;

/**
 * ByteAccess based on a {@link Ntree}<Byte>.
 *
 * @author Tobias Pietzsch
 */
public final class ByteNtree implements ByteAccess, NtreeAccess< Byte, ByteNtree >
{
	private final long[] position;

	private final Ntree< Byte > data;

	public ByteNtree( final long[] dimenions, final long[] position, final Byte value )
	{
		this.data = new Ntree< Byte >( dimenions, value );
		this.position = position;
	}

	/* Copy constructor */
	private ByteNtree( final Ntree< Byte > data, final long[] position )
	{
		this.data = data;
		this.position = position;
	}

	@Override
	public byte getValue( final int index )
	{
		// ignore index, get tree position from RandomAccess/Cursor
		return data.getNode( position ).getValue();
	}

	@Override
	public void setValue( final int index, final byte value )
	{
		// ignore index, get tree position from RandomAccess/Cursor
		data.createNodeWithValue( position, value );
	}

	@Override
	public Ntree< Byte > getCurrentStorageNtree()
	{
		return data;
	}

	@Override
	public ByteNtree createInstance( final long[] position )
	{
		return new ByteNtree( data, position );
	}
}
