/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package android.raywenderlich.com.puppyparadise

import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.puppy_item.view.*

class PuppyAdapter : RecyclerView.Adapter<PuppyAdapter.PuppyViewHolder>() {

  companion object {
    private const val THROTTLE_DURATION_MILLIS = 500
  }

  interface ItemClickListener {
    fun onItemClicked(position: Int)
  }

  private val items = mutableListOf<Puppy>()

  private var lastClickedTime = 0L

  private lateinit var listener: ItemClickListener

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuppyViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.puppy_item, parent, false)
    return PuppyViewHolder(view)
  }

  fun setListener(listener: ItemClickListener) {
    this.listener = listener
  }

  fun setData(puppies: List<Puppy>) {
    items.clear()
    items.addAll(puppies)
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: PuppyViewHolder, position: Int) = holder.showData(items[position], position)

  inner class PuppyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun showData(puppy: Puppy, position: Int) = with(itemView) {
      Picasso.get()
          .load(puppy.imageResource)
          .into(puppyImage)

      heartImage.visibility = if (puppy.isLiked) View.VISIBLE else View.GONE

      itemView.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickedTime >= THROTTLE_DURATION_MILLIS) {
          lastClickedTime = SystemClock.elapsedRealtime()
          listener.onItemClicked(position)
        }
      }
    }
  }
}