package cs481.beerpal

interface BeerRepositoryListener {
    fun dataListUpdated()
    fun dataListResult(beerList: ArrayList<Beer>)
}