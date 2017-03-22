package DimenReduceCSV

/**
  * Created by luo024 on 21/03/2017.
  */

class percentile {
  def computePercentile(vals: Seq[Double], tile: Double, unsorted: Boolean = true): Double = {
    assert(tile >= 0 && tile <= 100)
    if (vals.isEmpty) Double.NaN
    else {
      assert(vals.nonEmpty)
      // Make sure the list is sorted, if that's what we've been told
      if (!unsorted && vals.length >= 2) vals.sliding(2).foreach(l => assert(l(0) <= l(1))) else {}
      // NIST method; data to be sorted in ascending order
      val r =
        if (unsorted) vals.sorted
        else vals
      val length = r.length
      if (length == 1) r.head
      else {
        val n = (tile / 100d) * (length - 1)
        val k = math.floor(n).toInt
        val d = n - k
        if (k <= 0) r.head
        else {
          val last = length
          if (k + 1 >= length) {
            r.last
          } else {
            r(k) + d * (r(k + 1) - r(k))
          }
        }
      }
    }
  }
}
