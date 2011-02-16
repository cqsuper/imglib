/**
 * Copyright (c) 2011, Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the imglib project nor
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

package mpicbg.imglib;

import mpicbg.imglib.outofbounds.OutOfBoundsFactory;
import mpicbg.imglib.outofbounds.RealOutOfBoundsFactory;

/**
 * Implements {@link RandomAccessible} for a {@link RandomAccessibleInterval}
 * through an {@link OutOfBoundsFactory}.
 *
 * @author Stephan Saalfeld <saalfeld@mpi-cbg.de>
 * @version 0.1a
 */
final public class ExtendedRealRandomAccessibleRealInterval< T, F extends RealRandomAccessibleRealInterval< T, F > > implements RealInterval, RealRandomAccessible< T > 
{
	final protected F interval;
	final protected RealOutOfBoundsFactory< T, F > factory;

	public ExtendedRealRandomAccessibleRealInterval( final F interval, final RealOutOfBoundsFactory< T, F > factory )
	{
		this.interval = interval;
		this.factory = factory;
	}
	
	@Override
	final public double realMax( final int d )
	{
		return interval.realMax( d );
	}

	@Override
	final public void realMax( final double[] max )
	{
		realMax( max );
	}

	@Override
	final public double realMin( final int d )
	{
		return interval.realMin( d );
	}

	@Override
	final public void realMin( final double[] min )
	{
		realMin( min );
	}

	@Override
	final public int numDimensions()
	{
		return interval.numDimensions();
	}

	@Override
	final public RealRandomAccess< T > realRandomAccess()
	{
		return interval.realRandomAccess( factory );
	}
}