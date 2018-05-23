package neuralNetwork;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Random;

public class Matrix implements Serializable {
    private final int rows;
    private final int cols;
    private final double data[][];

    /**
     * Create a new matrix with dimensions of rows x cols with full of 0's.
     *
     * @param rows number of rows.
     * @param cols number of columns.
     */
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    /**
     * Creates a new matrix with specific data.
     *
     * @param data data.
     */
    public Matrix(double[][] data) {
        rows = data.length;
        cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, data[i].length);
        }
    }

    /**
     * Sets all elements to zero of a matrix.
     */
    public void setToZero() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = 0;
            }
        }
    }

    /**
     * Returns the element at the specified position in the matrix.
     *
     * @param row which row.
     * @param col which column.
     * @return the element at the specified position.
     */
    public double get(int row, int col) {
        if (row >= 0 && row < this.rows && col >= 0 && col < this.cols)
            return data[row][col];
        else
            throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Sets the element at the specified position in the matrix.
     *
     * @param row  which row.
     * @param col  which column.
     * @param data new data at the position.
     */
    public void set(int row, int col, double data) {
        if (row >= 0 && row < this.rows && col >= 0 && col < this.cols)
            this.data[row][col] = data;
        else
            throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * Returns the identity matrix with the given size.
     *
     * @param size number of rows and columns.
     * @return identity matrix.
     */
    static public Matrix identityMatrix(int size) {
        Matrix identity = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            identity.data[i][i] = 1;
        }

        return identity;
    }

    /**
     * Returns the sum of two matrices with the same dimensions
     *
     * @param other other operand.
     * @return resultant matrix.
     */
    public Matrix add(Matrix other) {
        if (this.cols != other.cols || this.rows != other.rows)
            throw new RuntimeException("Illegal matrix dimensions");

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] + other.data[i][j];
            }
        }

        return result;
    }

    /**
     * Multiply the matrix with a scalar value.
     *
     * @param scalar operand.
     * @return resultant matrix.
     */
    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] * scalar;
            }
        }

        return result;
    }

    /**
     * Multiply the matrix with another matrix.
     *
     * @param other other operand.
     * @return resultant matrix.
     */
    public Matrix multiply(Matrix other) {
        if (this.cols != other.rows)
            throw new RuntimeException("Illegal matrix dimensions");

        Matrix result = new Matrix(this.rows, other.cols); //Matrix full of zeros
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                for (int k = 0; k < this.cols; k++) {
                    result.data[i][j] += (this.data[i][k] * other.data[k][j]);
                }
            }
        }

        return result;
    }

    /**
     * Returns the transpose of the matrix.
     *
     * @return transpose of the matrix.
     */
    public Matrix transpose() {
        Matrix transposed = new Matrix(this.cols, this.rows);

        for (int i = 0; i < transposed.rows; i++) {
            for (int j = 0; j < transposed.cols; j++) {
                transposed.data[i][j] = this.data[j][i];
            }
        }
        return transposed;
    }

    /**
     * Fills the matrix with random double values between the parameters.
     *
     * @param lowerBound minimum.
     * @param upperBound maximum.
     */
    public void randomize(double lowerBound, double upperBound) {
        if (lowerBound > upperBound)
            return;
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = random.nextDouble() * (upperBound - lowerBound) + lowerBound;
            }
        }
    }

    /**
     * Prints the matrix to the standard output.
     */
    public void print() {

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.format("%.4f", data[i][j]);
                if (j != this.cols - 1)
                    System.out.print(" ");
            }
            System.out.println("");
        }
    }

    /**
     * Creates a one dimensional array from the matrix.
     *
     * @return double array.
     */
    public double[] toOneDimensionalArray() {
        double[] result = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(this.data[i], 0, result, i * cols, cols);
        }

        return result;
    }

    /**
     * Creates a matrix with the given rows and columns from a one dimensional array.
     *
     * @param array one dimensional array.
     * @param rows  number of rows of the matrix.
     * @param cols  number of columns of the matrix.
     * @return result matrix.
     */
    public static Matrix arrayToMatrix(double[] array, int rows, int cols) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(array, i * cols, result.data[i], 0, cols);
        }

        return result;
    }

    /**
     * Creates a vector (a matrix with a column/row of one) from a one-dimensional array.
     *
     * @param array        input array.
     * @param columnVector true: column vector
     *                     false: row vector
     * @return output vector (instance of matrix).
     */
    public static Matrix oneDimensionalArrayToVector(double[] array, boolean columnVector) {
        double data[][] = new double[1][array.length]; //Create a two-dimensional array
        data[0] = array;
        Matrix result = new Matrix(data); //Now this is a row vector
        if (columnVector)
            return result.transpose(); //If we need column vector we transpose it.
        else
            return result;

    }

    /**
     * Returns the maximum value in the matrix.
     *
     * @return maximum value.
     */
    public double getMaxValue() {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] > max)
                    max = data[i][j];
            }
        }
        return max;
    }

    /**
     * Returns the minimum value in the matrix.
     *
     * @return minimum value.
     */
    public double getMinValue() {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] < min)
                    min = data[i][j];
            }
        }
        return min;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix))
            return false;

        Matrix other = (Matrix) obj;
        if (this.rows == other.rows && this.cols == other.cols) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (data[i][j] != other.data[i][j])
                        return false;
                }
            }
            return true;
        } else
            return false;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getData() {
        return data;
    }

    /**
     * Returns the number of elements.
     *
     * @return number of elements.
     */
    public int getSize() {
        return rows * cols;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(3 * cols * rows);
        sb.append("Dimensions: ");
        sb.append(rows);
        sb.append("x");
        sb.append(cols);
        sb.append("\n");
        DecimalFormat numberFormat = new DecimalFormat("0.000");
        numberFormat.setPositivePrefix("+");
        for (double[] row : data) {
            for (int j = 0; j < row.length; j++) {
                sb.append(numberFormat.format(row[j]));
                if (j != row.length - 1)
                    sb.append(" ");
                else
                    sb.append("\n");
            }
        }
        return sb.toString();
    }
}

