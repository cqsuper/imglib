/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package net.imglib2.algorithm.fft;

import net.imglib2.util.Util;

/**
 * TODO
 *
 * @author Stephan Preibisch
 */
public class PhaseCorrelationPeak implements Comparable<PhaseCorrelationPeak>
{
	long[] position = null;
	long[] originalInvPCMPosition = null;
	float phaseCorrelationPeak = 0, crossCorrelationPeak = 0;
	long numPixels = 0;
	boolean sortPhaseCorrelation = true;
	
	public PhaseCorrelationPeak( final long[] position, final float phaseCorrelationPeak, final float crossCorrelationPeak )
	{
		this.position = position.clone();
		this.phaseCorrelationPeak = phaseCorrelationPeak;
		this.crossCorrelationPeak = crossCorrelationPeak;
	}
	
	public PhaseCorrelationPeak( final long[] position, final float phaseCorrelationPeak )
	{
		this ( position, phaseCorrelationPeak, 0 );
	}

	public PhaseCorrelationPeak( final long[] position )
	{
		this ( position, 0, 0 );
	}
	
	public PhaseCorrelationPeak()
	{
		this ( null, 0, 0 );
	}
	
	public void setPosition( final long[] position ) { this.position = position.clone(); }
	public void setOriginalInvPCMPosition( final long[] originalInvPCMPosition ) { this.originalInvPCMPosition = originalInvPCMPosition; }
	public void setPhaseCorrelationPeak( final float phaseCorrelationPeak ) { this.phaseCorrelationPeak = phaseCorrelationPeak; }
	public void setCrossCorrelationPeak( final float crossCorrelationPeak ) { this.crossCorrelationPeak = crossCorrelationPeak; }
	public void setSortPhaseCorrelation( final boolean sortPhaseCorrelation ) { this.sortPhaseCorrelation = sortPhaseCorrelation; }
	public void setNumPixels( final long numPixels ) { this.numPixels = numPixels; }
	
	public long[] getPosition() { return position.clone(); }
	public long[] getOriginalInvPCMPosition() { return originalInvPCMPosition; }
	public float getPhaseCorrelationPeak() { return phaseCorrelationPeak; }
	public float getCrossCorrelationPeak() { return crossCorrelationPeak; }
	public boolean getSortPhaseCorrelation() { return sortPhaseCorrelation; }
	public long getNumPixels() { return numPixels; }

	@Override
	public int compareTo( final PhaseCorrelationPeak o )
	{
		if ( sortPhaseCorrelation )
		{
			if ( this.phaseCorrelationPeak > o.phaseCorrelationPeak )
				return 1;
			else if ( this.phaseCorrelationPeak == o.phaseCorrelationPeak )
				return 0;
			else
				return -1;
		}
		else
		{		
			if ( this.crossCorrelationPeak > o.crossCorrelationPeak )
			{
				return 1;
			}
			else if ( this.crossCorrelationPeak == o.crossCorrelationPeak )
			{
				if ( this.numPixels >= o.numPixels )
					return 1;
				else
					return 0;
			}
			else
			{
				return -1;
			}
		}
	}
	
	@Override
	public String toString()
	{
		if ( originalInvPCMPosition == null)
			return Util.printCoordinates( position ) + ", phaseCorrelationPeak = " + phaseCorrelationPeak + ", crossCorrelationPeak = " + crossCorrelationPeak;
		else
			return Util.printCoordinates( position ) + " [" + Util.printCoordinates( originalInvPCMPosition ) + "], phaseCorrelationPeak = " + phaseCorrelationPeak + ", crossCorrelationPeak = " + crossCorrelationPeak; 
	}
}
