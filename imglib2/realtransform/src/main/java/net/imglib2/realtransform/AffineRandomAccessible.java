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
import net.imglib2.RealLocalizable;
import net.imglib2.RealRandomAccess;
import net.imglib2.RealRandomAccessible;

/**
 * 
 *
 * @author ImgLib2 developers
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 * @author Tobias Pietzsch <tobias.pietzsch@gmail.com>
 */
public class AffineRandomAccessible< T, R extends AffineGet > extends RealTransformRandomAccessible< T, R >
{
	/**
	 * {@link RealRandomAccess} that generates its samples from a target
	 * {@link RealRandomAccessible} at coordinates transformed by a
	 * {@link RealTransform}.
	 *
	 */
	public class AffineRandomAccess extends RealTransformRandomAccessible< T, R >.RealTransformRandomAccess
	{
		final double[] move = new double[ n ];
		
		protected AffineRandomAccess()
		{
			super();
		}

		protected AffineRandomAccess( final AffineRandomAccess a )
		{
			super( a );
		}

		protected void scaleMove( final double distance, final int d )
		{
			final RealLocalizable dd = transform.d( d );
			for ( int ddd = 0; ddd < n; ++ddd )
				move[ ddd ] = distance * dd.getDoublePosition( ddd );
		}
		
		@Override
		public void fwd( final int d )
		{
			super.fwd( d );
			targetAccess.move( transform.d( d ) );
		}

		@Override
		public void bck( final int d )
		{
			super.bck( d );
			scaleMove( -1, d );
			targetAccess.move( move );
		}

		@Override
		public void move( final long distance, final int d )
		{
			super.move( distance, d );
			scaleMove( distance, d );
			targetAccess.move( move );
		}

		@Override
		public void move( final int distance, final int d )
		{
			move( ( long ) distance, d );
		}

		@Override
		public void move( final Localizable localizable )
		{
			super.move( localizable );
			apply();
		}

		@Override
		public void move( final int[] distance )
		{
			super.move( distance );
			apply();
		}

		@Override
		public void move( final long[] distance )
		{
			super.move( distance );
			apply();
		}

		@Override
		public void setPosition( final int[] pos )
		{
			super.setPosition( pos );
			apply();
		}

		@Override
		public void setPosition( final long[] pos )
		{
			super.setPosition( pos );
			apply();
		}

		@Override
		public void setPosition( final int position, final int d )
		{
			setPosition( ( long ) position, d );
		}

		@Override
		public void setPosition( final long pos, final int d )
		{
			final long distance = pos - position[ d ];
			move( distance, d );
		}

		@Override
		public void setPosition( final Localizable localizable )
		{
			localizable.localize( position );
			apply();
		}

		@Override
		public T get()
		{
			return targetAccess.get();
		}

		@Override
		public AffineRandomAccess copy()
		{
			return new AffineRandomAccess( this );
		}

		@Override
		public AffineRandomAccess copyRandomAccess()
		{
			return copy();
		}
	}
	
	public AffineRandomAccessible( final RealRandomAccessible< T > target, final R transform )
	{
		super( target, transform );
	}

	@Override
	public AffineRandomAccess randomAccess()
	{
		return new AffineRandomAccess();
	}

	/**
	 * To be overridden for {@link RealTransform} that can estimate the
	 * boundaries of a transferred {@link RealInterval}.
	 */
	@Override
	public AffineRandomAccess randomAccess( final Interval interval )
	{
		return randomAccess();
	}	
}
