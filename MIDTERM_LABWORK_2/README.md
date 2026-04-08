# 3x3 Matrix Determinant Solver

## Student Information
- **Name:** Aspiras, Weiam D.
- **Course:** Math 101 – Linear Algebra
- **School:** University of Perpetual Help System DALTA, Molino Campus
- **Assignment:** Programming Assignment 1 – 3x3 Matrix Determinant Solver
- **Date Completed:** March 18, 2026

---

## Assigned Matrix

```
┌               ┐
│   5   2   1  │
│   3   4   2  │
│   1   2   3  │
└               ┘
```

---

## Solution Method

This program computes the determinant of the assigned 3×3 matrix using **cofactor expansion along the first row**:

$$\det(A) = a_{11}M_{11} - a_{12}M_{12} + a_{13}M_{13}$$

Where:
- $M_{11}$, $M_{12}$, $M_{13}$ are the 2×2 minors obtained by removing row 0 and columns 0, 1, 2 respectively
- Each minor is calculated using the 2×2 determinant formula: $\det = ad - bc$

### Step-by-Step Calculation:

**Step 1 – Minor M₁₁:** Remove row 0 and column 0
```
det([4,2],[2,3]) = (4×3) - (2×2) = 12 - 4 = 16
```

**Step 2 – Minor M₁₂:** Remove row 0 and column 1
```
det([3,2],[1,3]) = (3×3) - (2×1) = 9 - 2 = 7
```

**Step 3 – Minor M₁₃:** Remove row 0 and column 2
```
det([3,4],[1,2]) = (3×2) - (4×1) = 6 - 4 = 2
```

**Cofactor Terms:**
```
C₁₁ = (+1) × 5 × 8 = 40
C₁₂ = (-1) × 2 × 7 = -14
C₁₃ = (+1) × 1 × 2 = 2
```

**Final Determinant:**
```
det(M) = 40 + (-14) + 2 = 28
```

---

## How to Run

### Java Program

```bash
javac app.java
java app
```

### JavaScript Program

Requires Node.js to be installed on your system.

```bash
node scripts.js
```

---

## Sample Output

Both programs produce identical console output:

```
====================================================
  3x3 MATRIX DETERMINANT SOLVER
  Student: [YOUR FULL NAME]
  Assigned Matrix:
====================================================
┌               ┐
│   5   2   1  │
│   3   4   2  │
│   1   2   3  │
└               ┘
====================================================
  Step 1 ? Minor M??: det([4,2],[2,3]) = (4×3)-(2×2) = 8
  Step 2 ? Minor M??: det([3,2],[1,3]) = (3×3)-(2×1) = 7
  Step 3 ? Minor M??: det([3,4],[1,2]) = (3×2)-(4×1) = 2

  Cofactor C?? = (+1) × 5 × 8 = 40
  Cofactor C?? = (-1) × 2 × 7 = -14
  Cofactor C?? = (+1) × 1 × 2 = 2

  det(M) = 40 + (-14) + 2
====================================================
  ?  DETERMINANT = 28
====================================================
```

---

## Final Result

**The determinant of the assigned matrix is: `28`**

Since the determinant is non-zero (28 ≠ 0), the matrix is **invertible** (non-singular).

---

## Files Included

- `app.java` – Java implementation of the determinant solver
- `scripts.js` – JavaScript implementation of the determinant solver
- `README.md` – This documentation file

---

## Key Concepts

1. **Cofactor Expansion:** A method to calculate determinants by expanding along a row or column
2. **2×2 Determinant:** The basic formula $\det = ad - bc$ for a 2×2 matrix
3. **Minor:** A submatrix obtained by deleting rows and columns
4. **Singular Matrix:** A matrix with determinant = 0 (non-invertible)
5. **Non-Singular Matrix:** A matrix with determinant ≠ 0 (invertible)

