
package net.imglib2.script.analysis;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import net.imglib2.algorithm.scalespace.DifferenceOfGaussian;
import net.imglib2.algorithm.scalespace.DifferenceOfGaussianPeak;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.function.RealTypeConverter;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.script.algorithm.fn.AlgorithmUtil;

/** Perform a difference of Gaussian on the given {@link Image}, and this class itself
 * becomes the {@link List} of found peaks, each as a float[] array that specifies its position.
 * 
 * See also {@link DifferenceOfGaussian, DifferenceOfGaussianPeak}.
 *
 */
public class DoGPeaks<N extends RealType<N>> extends ArrayList<float[]>
{
	private static final long serialVersionUID = 7614417748092214062L;

	private final List<DifferenceOfGaussianPeak<FloatType>> peaks;

	@SuppressWarnings("unchecked")
	public DoGPeaks(final Object fn, final Number sigmaLarge, final Number sigmaSmall,
			final Number minPeakValue, final Number normalizationFactor) throws Exception {
		this(AlgorithmUtil.wrapS(fn), sigmaLarge, sigmaSmall, minPeakValue, normalizationFactor);
	}

	/** Consider the {@param img} as isotropic: apply the same sigma to all dimensions. */
	public DoGPeaks(final Img<N> img, final Number sigmaLarge, final Number sigmaSmall,
			final Number minPeakValue, final Number normalizationFactor) throws Exception {
		this(img, AlgorithmUtil.asArray(img.numDimensions(), sigmaLarge.doubleValue()),
				AlgorithmUtil.asArray(img.numDimensions(), sigmaSmall.doubleValue()),
				minPeakValue, normalizationFactor);
	}

	/** Consider the {@param fn} as an isotropic image: apply the same sigma to all dimensions. */
	public DoGPeaks(final Object fn, final List<Number> sigmaLarge, final List<Number> sigmaSmall,
			final Number minPeakValue, final Number normalizationFactor) throws Exception {
		this(fn, AlgorithmUtil.asDoubleArray(sigmaLarge),
				AlgorithmUtil.asDoubleArray(sigmaSmall),
				minPeakValue, normalizationFactor);
	}
	
	/** Consider the {@param fn} as an isotropic image: apply the same sigma to all dimensions. */
	public DoGPeaks(final Object fn, final float[] sigmaLarge, final float[] sigmaSmall,
			final Number minPeakValue, final Number normalizationFactor) throws Exception {
		this(fn, AlgorithmUtil.asDoubleArray(sigmaLarge),
				AlgorithmUtil.asDoubleArray(sigmaSmall),
				minPeakValue, normalizationFactor);
	}

	@SuppressWarnings("unchecked")
	public DoGPeaks(final Object fn, final double[] sigmaLarge, final double[] sigmaSmall,
			final Number minPeakValue, final Number normalizationFactor) throws Exception {
		this(AlgorithmUtil.wrapS(fn), sigmaLarge, sigmaSmall, minPeakValue, normalizationFactor);
	}

	/**
	 *  @param img The {@link Image} to search peaks into.
	 *  @param sigmaLarge A double[] array with a sigma value for each dimension of the image.
	 *  @param sigmaSmall A double[] array with a sigma value for each dimension of the image.
	 *  @param minPeakValue The lowest intensity value for a difference of Gaussian peak to be considered so.
	 *  @param normalizationFactor See {@link DifferenceOfGaussian}. */
	public DoGPeaks(final Img<N> img, final double[] sigmaLarge, final double[] sigmaSmall,
						final Number minPeakValue, final Number normalizationFactor) throws Exception {
		final DifferenceOfGaussian<N, FloatType> dog
			= new DifferenceOfGaussian<N, FloatType>(img, new ImageFactory<FloatType>(new FloatType(), new ArrayContainerFactory()),
													 new RealTypeConverter<N, FloatType>(), new OutOfBoundsStrategyMirrorFactory<FloatType>(),
													 sigmaLarge, sigmaSmall,
													 new FloatType(minPeakValue.floatValue()), new FloatType(normalizationFactor.floatValue()));
		if (!dog.process()) {
			throw new Exception("Could not process DifferenceOfGaussian: " + dog.getErrorMessage());
		}

		this.peaks = dog.getPeaks();
		
		for (final DifferenceOfGaussianPeak<FloatType> p : this.peaks) {
			this.add(p.getSubPixelPosition());
		}
	}

	/** Get all peaks as {@link DifferenceOfGaussianPeak} instances. */
	public List<DifferenceOfGaussianPeak<FloatType>> getPeaks() {
		return new ArrayList<DifferenceOfGaussianPeak<FloatType>>(this.peaks);
	}

	/** Returns a list of peaks as Java3D points if possible:
	 * 
	 * {@link Point2f} for 2D images
	 * {@link Point3f} for 3D images
	 * {@link Point4f} for 4D images
	 * 
	 * @throws IllegalArgumentException if scalingFactor.length is different than the image dimensions.
	 * @throws Exception if the image dimensions is not 2, 3, or 4.
	 */
	public List<Object> asPoints(final float[] scalingFactor) throws Exception {
		final List<Object> points = new ArrayList<Object>();
		if (this.isEmpty()) return points;
		int len = this.get(0).length;
		if (len < 2 || len > 4) {
			throw new Exception("Dimensions of the peak coordinates are not 2, 3 or 4. Use getDoGPeaks() instead.");
		}
		if (scalingFactor.length != len) {
			throw new IllegalArgumentException("scalingFactor dimensions does not match with the dimensions of the peaks.");
		}

		switch (len) {
			case 2:
				for (final float[] p : this)
					points.add(new Point2f(scaled(p, scalingFactor)));
				break;
			case 3:
				for (final float[] p : this)
					points.add(new Point3f(scaled(p, scalingFactor)));
				break;
			case 4:
				for (final float[] p : this)
					points.add(new Point4f(scaled(p, scalingFactor)));
				break;
		}

		return points;
	}

	/** Returns a list of peaks as Java3D points: Point2f for 2D images, Point3f for 3D images, and Point4f for 4D images.
	 * For images of 1D or more than 4D, returns a list of float[]. */
	public List<Object> asPoints() throws Exception {
		return asPoints(1);
	}

	/** Returns a list of peaks as Java3D points: Point2f for 2D images, Point3f for 3D images, and Point4f for 4D images.
	 * For images of 1D or more than 4D, returns a list of float[]. */
	public List<Object> asPoints(final Number scalingFactor) throws Exception {
		if (this.isEmpty()) return new ArrayList<Object>();
		float[] s = new float[this.get(0).length];
		for (int i=0; i<s.length; i++) s[i] = scalingFactor.floatValue();
		return asPoints(s);
	}

	private static final float[] scaled(final float[] peak, final float[] scalingFactor) {
		final float[] p = new float[peak.length];
		for (int i=0; i<peak.length; i++) {
			p[i] = peak[i] * scalingFactor[i];
		}
		return p;
	}
}
