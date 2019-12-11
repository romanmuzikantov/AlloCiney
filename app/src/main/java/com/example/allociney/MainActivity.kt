package com.example.allociney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allociney.item.MovieItem
import com.example.allociney.model.Movie
import com.example.allociney.network.MovieService
import com.example.allociney.network.data.MovieResult
import com.google.gson.Gson
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var movieService: MovieService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listMovies = mutableListOf<Movie>()

        listMovies.add(Movie("Alien", "Ridley Scott", 1979))
        listMovies.add(Movie("La cité de la peur", "Alain Berberian", 1994))
        listMovies.add(Movie("Space balls", "Mel Brooks", 1987))
        listMovies.add(Movie("C'est arrivé près de chez vous", "Benoît Poelvoorde", 1992))
        listMovies.add(Movie("Beetlejuice", "Tim Burton", 1988))
        listMovies.add(Movie("Eraserhead", "David Lynch", 1977))
        listMovies.add(Movie("Les aventures de Rabbi Jacob", "Gérard Qury", 1973))
        listMovies.add(Movie("Les bouchers verts", "Anders-Thomas Jensen", 2003))
        listMovies.add(Movie("Risky Business", "Paul Brickman", 1983))
        listMovies.add(Movie("Les tontons flingueurs", "Georges Lautner", 1979))
        listMovies.add(Movie("The Rocky Horror Picture Show", "Jim Sharman", 1975))
        listMovies.add(Movie("Legend", "Ridley Scott", 1985))
        listMovies.add(Movie("Labyrinth", "Jim Henson", 1986))
        listMovies.add(Movie("L'histoire sans fin", "Wolfgang Peterson", 1984))
        listMovies.add(Movie("Dark Crystal", "Jim Henson", 1982))
        listMovies.add(Movie("Fargo", "Coen", 1996))
        listMovies.add(Movie("Breakfast Club", "John Hughes", 1985))
        listMovies.add(Movie("Délivrance", "John Boorman", 1973))
        listMovies.add(Movie("The blair witch project", "Eduardo Sanchez, Daniel Myrick", 1999))

        val movieItemAdapter = ItemAdapter<MovieItem>()
        movieItemAdapter.add(listMovies.map {
            MovieItem(it)
        })

        moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        //moviesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val fastAdapter = FastAdapter.with(movieItemAdapter)
        moviesRecyclerView.adapter = fastAdapter
        //moviesRecyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
        moviesRecyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        fastAdapter.onClickListener = { _: View?, _: IAdapter<MovieItem>,
                                        movieItem: MovieItem, _: Int ->
            val intent = MovieActivity.createIntent(this, movieItem.movie)
            startActivity(intent)
            true
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieService = retrofit.create(MovieService::class.java)

        // SYNCHRONE
        //val result = movieService.getMovies("alien").execute().body()

        val result = movieService.getMovies("alien").enqueue(object: Callback<MovieResult> {
            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
                Log.e("AlloCiney", "error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<MovieResult>, response: Response<MovieResult>) {
                val movies = response.body()?.results ?: emptyArray()
            }

        })
    }
}
