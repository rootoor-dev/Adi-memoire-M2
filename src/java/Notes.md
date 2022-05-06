*Sparse Vector*: List of nonzero elements with their
index locations. It assumes no particular ordering of elements.

*COOR Matrix*: Coordinate Storage Matrix. List of
nonzero elements with their respective row and
column indices. This is the most general sparse
matrix format, but it is not very space or computationally efficient. It assumes no ordering of
nonzero matrix values.

*CRS Matrix* : Compressed Row Storage Matrix.
Subsequent nonzeros of the matrix rows are
stored in contiguous memory locations and an
additional integer arrays specifies where each row
begins. It assumes no ordering among nonzero
values within each row, but rows are stored in
consecutive order.

*CCS Matrix*: Compressed Column Storage (also commonly known as the Harwel l-Boeing sparse matrix format [4]). This is a variant of CRS storage
where columns, rather rows, are stored contiguously. Note that the CCS ordering of A is the
same as the CRS of AT.

*CDS Matrix*: Compressed Diagonal Storage. Designed primarily for matrices with relatively constant bandwidth, the sub and super-diagonals
are stored in contiguous memory locations.

*JDS Matrix*: Jagged Diagonal Storage. Also know as
ITPACK storage. More space efficient than CDS IML++
package matrices at the cost of a gather/scatter operation.

*BCRS Matrix*: Block Compressed Row Storage. Useful when the sparse matrix is comprised of square
dense blocks of nonzeros in some regular pattern.
The savings in storage and reduced indirect addressing over CRS can be significant for matrices
with large block sizes.

*SKS Matrix*: Skyline Storage. Also for variable band
or profile matrices. Mainly used in direct solvers,
but can also be used for handling the diagonal
blocks in block matrix 
