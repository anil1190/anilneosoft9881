package com.example.myapplication.ui.component

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.myapplication.R
import com.example.myapplication.data.Resource
import com.example.myapplication.data.dto.Recipes
import com.example.myapplication.data.dto.RecipesItem
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ViewOverlayableImageBinding
import com.example.myapplication.ui.BaseActivity
import com.example.myapplication.ui.component.adapter.RecipesAdapter
import com.example.myapplication.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesAdapter

    private lateinit var layoutManager1: LinearLayoutManager
    private lateinit var adapter: CarouselAdapter
    private lateinit var snapHelper: SnapHelper
    private var mSearchList = ArrayList<RecipesItem>()

    override fun observeViewModel() {
        observe(mainViewModel.recipesLiveData, ::handleRecipesList)
        observeSnackBarMessages(mainViewModel.showSnackBar)
        observeToast(mainViewModel.showToast)
    }

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipesList.layoutManager = layoutManager
        binding.rvRecipesList.setHasFixedSize(true)
        mainViewModel.getRecipes()

        getCarouselImageList()
        getSearchedData()
    }

    private fun getCarouselImageList(){
        val images: ArrayList<Image> = ArrayList(ImageData.images.shuffled())
        layoutManager1 = ProminentLayoutManager(this)
        adapter = CarouselAdapter(images)
        snapHelper = PagerSnapHelper()

        with(binding.recyclerView) {
            setItemViewCacheSize(4)
            layoutManager = this@MainActivity.layoutManager1
            adapter = this@MainActivity.adapter

            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())

            snapHelper.attachToRecyclerView(this)
        }
        var pos = 2
        initRecyclerViewPosition(pos)
    }

    private fun initRecyclerViewPosition(position: Int) {
        // This initial scroll will be slightly off because it doesn't respect the SnapHelper.
        // Do it anyway so that the target view is laid out, then adjust onPreDraw.
        layoutManager1.scrollToPosition(position)

        binding.recyclerView.doOnPreDraw {
            val targetView = layoutManager1.findViewByPosition(position) ?: return@doOnPreDraw
            val distanceToFinalSnap =
                snapHelper.calculateDistanceToFinalSnap(layoutManager1, targetView)
                    ?: return@doOnPreDraw

            layoutManager1.scrollToPositionWithOffset(position, -distanceToFinalSnap[0])
        }

        binding.recyclerView.addItemDecoration(CirclePagerIndicatorDecoration())
    }

    private fun getSearchedData(){
        binding.searchView.queryHint = getString(R.string.search_hint)
        var recipesList = ArrayList<RecipesItem>()
        binding.searchView.isIconified = true
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                recipesList.clear()
                mSearchList?.let {
                    if (it.isNotEmpty()) {
                        for (recipe in it) {
                            if (recipe.name.lowercase(Locale.getDefault()).contains(query.lowercase(
                                    Locale.getDefault()))) {

                                recipesList.add(recipe)

                            }
                        }
                    }

                    if (recipesList.isNullOrEmpty()){

                    }else{
                        recipesAdapter = RecipesAdapter(mainViewModel, recipesList)
                        binding.rvRecipesList.adapter = recipesAdapter
                        showDataView(true)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }

        })

        val searchCloseButtonId: Int = searchView.getContext().getResources()
            .getIdentifier("android:id/search_close_btn", null, null)
        val closeButton: ImageView = this.searchView.findViewById(searchCloseButtonId) as ImageView

        closeButton.setOnClickListener(View.OnClickListener {
            recipesList.clear()
            observe(mainViewModel.recipesLiveData, ::handleRecipesList)
            recipesAdapter.notifyDataSetChanged()
            binding.searchView.setQuery("",false)
            binding.searchView.clearFocus()
            closeButton.visibility = View.GONE
        })


    }


    private fun bindListData(recipes: Recipes) {
        if (!(recipes.recipesList.isNullOrEmpty())) {
            mSearchList.addAll(recipes.recipesList)
            recipesAdapter = RecipesAdapter(mainViewModel, recipes.recipesList)
            binding.rvRecipesList.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.rvRecipesList.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvRecipesList.toGone()
    }


    private fun handleRecipesList(status: Resource<Recipes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { mainViewModel.showToastMessage(it) }
            }
        }
    }

}



/** Works best with a [LinearLayoutManager] in [LinearLayoutManager.HORIZONTAL] orientation */
class LinearHorizontalSpacingDecoration(@Px private val innerSpacing: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        outRect.left = if (itemPosition == 0) 0 else innerSpacing / 2
        outRect.right = if (itemPosition == state.itemCount - 1) 0 else innerSpacing / 2
    }
}

/** Offset the first and last items to center them */
class BoundsOffsetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        // It is crucial to refer to layoutParams.width (view.width is 0 at this time)!
        val itemWidth = view.layoutParams.width
        val offset = (parent.width - itemWidth) / 2

        if (itemPosition == 0) {
            outRect.left = offset
        } else if (itemPosition == state.itemCount - 1) {
            outRect.right = offset
        }
    }
}

internal class CarouselAdapter(private val images: List<Image>) :
    RecyclerView.Adapter<CarouselAdapter.VH>() {

    private var hasInitParentDimensions = false
    private var maxImageWidth: Int = 0
    private var maxImageHeight: Int = 0
    private var maxImageAspectRatio: Float = 1f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        // At this point [parent] has been measured and has valid width & height
        if (!hasInitParentDimensions) {
            maxImageWidth =
                parent.width - 2 * parent.resources.getDimensionPixelSize(R.dimen.gradient_width)
            maxImageHeight = parent.height
            maxImageAspectRatio = maxImageWidth.toFloat() / maxImageHeight.toFloat()
            hasInitParentDimensions = true
        }

        return VH(OverlayableImageView(parent.context))
    }

    override fun onBindViewHolder(vh: VH, position: Int) {
        val image = images[position]

        // Change aspect ratio
        val imageAspectRatio = image.aspectRatio
        val targetImageWidth: Int = if (imageAspectRatio < maxImageAspectRatio) {
            // Tall image: height = max
            (maxImageHeight * imageAspectRatio).roundToInt()
        } else {
            // Wide image: width = max
            maxImageWidth
        }
        vh.overlayableImageView.layoutParams = RecyclerView.LayoutParams(
            targetImageWidth,
            RecyclerView.LayoutParams.MATCH_PARENT
        )

        // Load image
        vh.overlayableImageView.image = image

        vh.overlayableImageView.setOnClickListener {
            val rv = vh.overlayableImageView.parent as RecyclerView
            rv.smoothScrollToCenteredPosition(position)
        }
    }

    override fun getItemCount(): Int = images.size

    class VH(val overlayableImageView: OverlayableImageView) :
        RecyclerView.ViewHolder(overlayableImageView)
}

private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
            val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
            val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)

            return (dxToStart + dxToEnd) / 2
        }
    }

    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}


/**
 * Arranges items so that the central one appears prominent: its neighbors are scaled down.
 * Based on https://stackoverflow.com/a/54516315/2291104
 */
internal class ProminentLayoutManager(
    context: Context,

    /**
     * This value determines where items reach the final (minimum) scale:
     * - 1f is when their center is at the start/end of the RecyclerView
     * - <1f is before their center reaches the start/end of the RecyclerView
     * - >1f is outside the bounds of the RecyclerView
     * */
    private val minScaleDistanceFactor: Float = 1.5f,

    /** The final (minimum) scale for non-prominent items is 1-[scaleDownBy] */
    private val scaleDownBy: Float = 0.5f
) : LinearLayoutManager(context, HORIZONTAL, false) {

    private val prominentThreshold =
        context.resources.getDimensionPixelSize(R.dimen.prominent_threshold)

    override fun onLayoutCompleted(state: RecyclerView.State?) =
        super.onLayoutCompleted(state).also { scaleChildren() }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) = super.scrollHorizontallyBy(dx, recycler, state).also {
        if (orientation == HORIZONTAL) scaleChildren()
    }

    private fun scaleChildren() {
        val containerCenter = width / 2f

        // Any view further than this threshold will be fully scaled down
        val scaleDistanceThreshold = minScaleDistanceFactor * containerCenter

        var translationXForward = 0f

        for (i in 0 until childCount) {
            val child = getChildAt(i)!!

            val childCenter = (child.left + child.right) / 2f
            val distanceToCenter = abs(childCenter - containerCenter)

            child.isActivated = distanceToCenter < prominentThreshold

            val scaleDownAmount = (distanceToCenter / scaleDistanceThreshold).coerceAtMost(1f)
            val scale = 1f - scaleDownBy * scaleDownAmount

            child.scaleX = scale
            child.scaleY = scale

            val translationDirection = if (childCenter > containerCenter) -1 else 1
            val translationXFromScale = translationDirection * child.width * (1 - scale) / 2f
            child.translationX = translationXFromScale + translationXForward

            translationXForward = 0f

            if (translationXFromScale > 0 && i >= 1) {
                // Edit previous child
                getChildAt(i - 1)!!.translationX += 2 * translationXFromScale

            } else if (translationXFromScale < 0) {
                // Pass on to next child
                translationXForward = 2 * translationXFromScale
            }
        }
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
        // Since we're scaling down items, we need to pre-load more of them offscreen.
        // The value is sort of empirical: the more we scale down, the more extra space we need.
        return (width / (1 - scaleDownBy)).roundToInt()
    }
}

class OverlayableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewOverlayableImageBinding.inflate(LayoutInflater.from(context), this)

    var image: Image? = null
        set(value) {
            field = value
            value?.let {
                Glide.with(binding.imageView)
                    .load(it.url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .transform(
                        FitCenter(),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.rounded_corners_radius))
                    )
                    .into(binding.imageView)
            }
        }

    override fun setActivated(activated: Boolean) {
        val isChanging = activated != isActivated
        super.setActivated(activated)
    }
}