import React from 'react'
import Login from '../views/login'
import Home from '../views/home'
import CadastroLancamento from '../views/lancamentos/cadastroLancamentos'
import CadastroUsuario from '../views/cadastroUsuario'
import {Route, Switch, HashRouter} from 'react-router-dom'
import ConsultaLancamentos from '../views/lancamentos/consulta-lancamentos'

function Rotas(){
    return(
        <HashRouter>
            <Switch>
                <Route path = "/login" component={Login}/>
                <Route path = "/cadastro-usuario" component={CadastroUsuario}/>
                <Route path = "/home" component={Home}/>
                <Route path = "/consulta-lancamentos" component={ConsultaLancamentos}/>
                <Route path = "/cadastro-lancamento/:id?" component={CadastroLancamento}/>
            </Switch>
        </HashRouter>
    )
}

export default Rotas