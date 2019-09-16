package com.example.peluchitosenterprise

import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Comunicador {

    var peluchitos: MutableList<Peluchitos> = java.util.ArrayList()
    var peluchito_actual:Peluchitos ?= null
    var inventario : ArrayList<String> = java.util.ArrayList()
    var bolB = false
    var bolE = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        val manager = supportFragmentManager
        val transaction=manager.beginTransaction()
        val agregarFragment = AgregarFragment()
        transaction.add(R.id.Contenedor,agregarFragment).commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val manager = supportFragmentManager
        val transaction=manager.beginTransaction()

        when (item.itemId) {
            R.id.nav_agregar-> {
                val agregarFragment = AgregarFragment()
                transaction.replace(R.id.Contenedor,agregarFragment).commit()

            }
            R.id.nav_buscar-> {
                val buscarFragment = BuscarFragment()
                transaction.replace(R.id.Contenedor,buscarFragment).commit()
            }
            R.id.nav_eliminar -> {
                val eliminarFragment = EliminarFragment()
                transaction.replace(R.id.Contenedor,eliminarFragment).commit()
            }
            R.id.nav_inventario -> {
                val inventarioFragment = InventarioFragment()
                enviarInventario(peluchitos)
                val args = Bundle()
                args.putStringArrayList("lista",inventario)

                inventarioFragment.arguments = args
                transaction.replace(R.id.Contenedor,inventarioFragment).commit()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun enviardatos1(id: String, nombre: String, cantidad: String, precio: String) {
        var peluchito = Peluchitos(id,nombre,cantidad,precio)
        peluchitos.add(peluchito)
    }

    override fun enviardatos2(nombre: String) {
        buscarpeluche(nombre,peluchitos)
        val args = Bundle()
        if(bolB){
            args.putString("key","True")
            args.putString("id",peluchito_actual?.id)
            args.putString("nombre",peluchito_actual?.nombre)
            args.putString("cantidad",peluchito_actual?.cantidad)
            args.putString("precio",peluchito_actual?.precio)
        }else{
            args.putString("key","False")
        }
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val buscarFragment = BuscarFragment()
        buscarFragment.arguments = args
        transaction.replace(R.id.Contenedor,buscarFragment).commit()
    }

    override fun enviardatos3(nombre: String) {
        eliminarpeluche(nombre,peluchitos)
        val args = Bundle()
        if(bolE.equals(true)){
            args.putString("key","True")
        }else {
            args.putString("key","False")
        }
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val eliminarFragment = EliminarFragment()
        eliminarFragment.arguments = args
        transaction.replace(R.id.Contenedor,eliminarFragment).commit()
    }

    private fun buscarpeluche(nombre:String,lista: MutableList<Peluchitos>){
        bolB = false
        for(i in lista){
            if (i.nombre.equals(nombre)) {
                peluchito_actual = Peluchitos(i.id, i.nombre, i.cantidad, i.precio)
                bolB = true
            }
        }
    }

    private fun eliminarpeluche(nombre:String, lista:MutableList<Peluchitos>){
        bolE = false
        for(i in lista){
            if(i.nombre!!.equals(nombre)){
                var index = lista.indexOf(i)
                lista.removeAt(index)
                bolE = true
                break
            }
        }
    }

    private fun enviarInventario(lista:MutableList<Peluchitos>){
        inventario.clear()
        for(i in lista ){
            var peluchito = "ID : "+i.id+"\n"+"Nombre : "+i.nombre+"\n"+"Cantidad : "+i.cantidad+"\n"+"Precio : "+i.precio
            var index = lista.indexOf(i)
            inventario?.add(index,peluchito)
        }
    }
}