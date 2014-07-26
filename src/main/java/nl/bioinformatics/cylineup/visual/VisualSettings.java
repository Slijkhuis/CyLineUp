package nl.bioinformatics.cylineup.visual;

import java.awt.Color;
import java.awt.Dimension;

public class VisualSettings {
	
	public static final int DONT_USE = 0;
	public static final int USE_FOR_FOLDCHANGE = 1;
	public static final int USE_FOR_PVALUE = 2;
	
	public static final int GRID_AUTO = 0;
	public static final int GRID_FIX_COLUMNS = 1;
	public static final int GRID_FIX_ROWS = 2;
	
	public static final int NODATA_SHOW = 0;
	public static final int NODATA_GREY = 1;
	public static final int NODATA_HIDE = 2;
	
	private int noDataNodes = 0;
	private int scale = 1;
	private float pValueCutOff = 0.001f;
	private int useFillColor = USE_FOR_FOLDCHANGE;
	private boolean useFillColorForUp = true;
	private boolean useFillColorForDown = true;
	private Color upColor = Color.green;
	private Color downColor = Color.red;
	private int useTransparency = DONT_USE;
	private int gridMode = GRID_AUTO;
	private int gridFixed = 2;
	private boolean autoSize = true;
	private int sizeWidth = 1080;
	private int sizeHeight = 1080;
	public Dimension calculatedSize = null;
	
	public boolean isAutoSize() {
		return autoSize;
	}
	public void setAutoSize(boolean autoSize) {
		this.autoSize = autoSize;
	}
	public int getSizeWidth() {
		return sizeWidth;
	}
	public void setSizeWidth(int sizeWidth) {
		this.sizeWidth = sizeWidth;
	}
	public int getSizeHeight() {
		return sizeHeight;
	}
	public void setSizeHeight(int sizeHeight) {
		this.sizeHeight = sizeHeight;
	}
	private double minValue;
	private double maxValue;
	private double minPValue;
	private double maxPValue;
	
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public float getpValueCutOff() {
		return pValueCutOff;
	}
	public void setpValueCutOff(float pValueCutOff) {
		this.pValueCutOff = pValueCutOff;
	}
	public int getUseFillColor() {
		return useFillColor;
	}
	public void setUseFillColor(int useFillColor) {
		this.useFillColor = useFillColor;
	}
	public boolean isUseFillColorForUp() {
		return useFillColorForUp;
	}
	public void setUseFillColorForUp(boolean useFillColorForUp) {
		this.useFillColorForUp = useFillColorForUp;
	}
	public boolean isUseFillColorForDown() {
		return useFillColorForDown;
	}
	public void setUseFillColorForDown(boolean useFillColorForDown) {
		this.useFillColorForDown = useFillColorForDown;
	}
	public Color getUpColor() {
		return upColor;
	}
	public void setUpColor(Color upColor) {
		this.upColor = upColor;
	}
	public Color getDownColor() {
		return downColor;
	}
	public void setDownColor(Color downColor) {
		this.downColor = downColor;
	}
	public int getUseTransparency() {
		return useTransparency;
	}
	public void setUseTransparency(int useTransparency) {
		this.useTransparency = useTransparency;
	}
	public int getGridMode() {
		return gridMode;
	}
	public void setGridMode(int gridMode) {
		this.gridMode = gridMode;
	}
	public int getGridFixed() {
		return gridFixed;
	}
	public void setGridFixed(int gridFixed) {
		this.gridFixed = gridFixed;
	}
	
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public double getMinPValue() {
		return minPValue;
	}
	public void setMinPValue(double minPValue) {
		this.minPValue = minPValue;
	}
	public double getMaxPValue() {
		return maxPValue;
	}
	public void setMaxPValue(double maxPValue) {
		this.maxPValue = maxPValue;
	}
	public int getNoDataNodes() {
		return noDataNodes;
	}
	public void setNoDataNodes(int noDataNodes) {
		this.noDataNodes = noDataNodes;
	}
}
