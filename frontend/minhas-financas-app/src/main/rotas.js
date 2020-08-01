import React from 'react'
import Login from '../views/login'
import Home from '../views/home'
import CadastroLancamento from '../views/lancamentos/cadastroLancamentos'
import CadastroUsuario from '../views/cadastroUsuario'
import {Route, Switch, HashRouter, Redirect} from 'react-router-dom'
import ConsultaLancamentos from '../views/lancamentos/consulta-lancamentos'
import AuthService from '../services/authService'
import {AuthConsumer} from '../main/provedorAutenticacao'

function RotaAutenticada({component: Component, isUsuarioAutenticado, ...props}){
    return (
        <Route {...props} render={(componentProps) => {
            if(isUsuarioAutenticado){
                return(
                    <Component {...componentProps}/>
                )
            } else {
                return (
                    <Redirect to={{pathname: '/login',state:{from: componentProps.location}}} />
                )
            }
        }}/>
    )
}

function Rotas(props){
    return(
        <HashRouter>
            <Switch>
                <Route path = "/login" component={Login}/>
                <Route path = "/cadastro-usuario" component={CadastroUsuario}/>
                <RotaAutenticada isUsuarioAutenticado={props.isUsuarioAutenticado} path = "/home" component={Home}/>
                <RotaAutenticada isUsuarioAutenticado={props.isUsuarioAutenticado} path = "/consulta-lancamentos" component={ConsultaLancamentos}/>
                <RotaAutenticada isUsuarioAutenticado={props.isUsuarioAutenticado} path = "/cadastro-lancamento/:id?" component={CadastroLancamento}/>
            </Switch>
        </HashRouter>
    )
}

export default () => (
    <AuthConsumer>
        { (context) => (<Rotas isUsuarioAutenticado={context.isAutenticado}/>) }
    </AuthConsumer>
)