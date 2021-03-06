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

package net.imglib2.realtransform;

import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.RealInterval;
import net.imglib2.RealRandomAccess;
import net.imglib2.RealRandomAccessible;

/**
 * 
 *
 * @author ImgLib2 developers
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 */
public class ConstantAffineRandomAccessible< T, R extends AffineGet > extends AffineRandomAccessible< T, R >
{
	final protected double[][] ds;
	final protected double[] affine;
	
	/**
	 * {@link RealRandomAccess} that generates its samples from a target
	 * {@link RealRandomAccessible} at coordinates transformed by a
	 * {@link RealTransform}.
	 *
	 */
	public class ConstantAffineRandomAccess extends AffineRandomAccessible< T, R >.AffineRandomAccess
	{
		protected ConstantAffineRandomAccess()
		{
			super();
		}
		
		@Override
		public void setPosition( final long[] pos )
		{
			assert pos.length >= n : "source and target dimensios do not match";
			for ( int r = 0, i = 0; r < n; ++r, ++i )
			{
				position[ r ] = pos[ r ];
				double ar = affine[ i++ ] * pos[ 0 ];
				for ( int c = 1; c < n; ++c, ++i )
					ar += affine[ i ] * pos[ c ];
				targetAccess.setPosition( ar + affine[ i ], r );
			}
		}
		
		@Override
		public void setPosition( final int[] pos )
		{
			assert pos.length >= n : "source and target dimensios do not match";
			for ( int r = 0, i = 0; r < n; ++r, ++i )
			{
				position[ r ] = pos[ r ];
				double ar = affine[ i++ ] * pos[ 0 ];
				for ( int c = 1; c < n; ++c, ++i )
					ar += affine[ i ] * pos[ c ];
				targetAccess.setPosition( ar + affine[ i ], r );
			}
		}
		
		@Override
		public void setPosition( final Localizable pos )
		{
			assert pos.numDimensions() >= n : "source and target dimensios do not match";
			for ( int r = 0, i = 0; r < n; ++r, ++i )
			{
				position[ r ] = pos.getLongPosition( r );
				double ar = affine[ i++ ] * pos.getLongPosition( 0 );
				for ( int c = 1; c < n; ++c, ++i )
					ar += affine[ i ] * pos.getLongPosition( c );
				targetAccess.setPosition( ar + affine[ i ], r );
			}
		}
		
		@Override
		protected void scaleMove( final double distance, final int d )
		{
			final double[] dd = ds[ d ];
			for ( int ddd = 0; ddd < n; ++ddd )
				move[ ddd ] = distance * dd[ ddd ];
		}
		
		@Override
		public void fwd( final int d )
		{
			++position[ d ];
			targetAccess.move( ds[ d ] );
		}
		

		@Override
		public ConstantAffineRandomAccess copy()
		{
			return new ConstantAffineRandomAccess();
		}

		@Override
		public ConstantAffineRandomAccess copyRandomAccess()
		{
			return copy();
		}
	}
	
	public ConstantAffineRandomAccessible( final RealRandomAccessible< T > target, final R transform )
	{
		super( target, transform );
		affine = transform.getRowPackedCopy();
		ds = new double[ transform.numSourceDimensions() ][];
		for ( int r = 0; r < ds.length; ++r )
		{
			final double[] d = new double[ ds.length ];
			transform.d( r ).localize( d );
			ds[ r ] = d;
		}
	}
	
	@Override
	public ConstantAffineRandomAccess randomAccess()
	{
		return new ConstantAffineRandomAccess();
	}

	/**
	 * To be overridden for {@link RealTransform} that can estimate the
	 * boundaries of a transferred {@link RealInterval}.
	 */
	@Override
	public ConstantAffineRandomAccess randomAccess( final Interval interval )
	{
		return randomAccess();
	}
}
