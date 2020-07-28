import React from 'react'

import UsuarioService from '../services/usuarioService.js'
import Card from '../components/card.js'
import FormGroup from '../components/form-group'
import {withRouter} from 'react-router-dom'
import {mensagemErro} from '../components/toastr'
import {mensagemSucesso} from '../components/toastr'

class CadastroUsuario extends React.Component{

    state = {
        nome : '',
        email : '',
        senha : '',
        senhaRepeticao : ''
    }

    constructor(){
        super();
        this.service = new UsuarioService();
    }

    cadastrar = () => {
        const {nome,email,senha,senhaRepeticao} = this.state
        const usuario = {nome ,email,senha,senhaRepeticao}

        try{
            this.service.validar(usuario)
        } catch(erro){
            const mensagens = erro.mensagens
            mensagens.forEach(msg => mensagemErro(msg))
            return false;
        }

        this.service.salvarUsuario(usuario).then(response => {
            mensagemSucesso('Usuario cadastrado com sucesso, realize o loguin para acessar o sistema')
            this.props.history.push('/login')
        }).catch(error => {
            mensagemErro(error.response.data);
        })
    }

    cancelar = () => {
        this.props.history.push('/login')
    }

    render(){
        return (
            <Card title="cadastro de usuario">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="bs-component">
                            <FormGroup label="Nome: *" htmlFor="inputNome">
                                <input type="text" 
                                        className="form-control" 
                                        name="nome"
                                        value={this.state.nome}
                                        onChange={e => this.setState({nome:e.target.value})}
                                        id="inputNome" 
                                        aria-describedby="emailHelp" 
                                        placeholder="Digite seu nome"/>
                            </FormGroup>
                            <FormGroup label="Email: *" htmlFor="inputEmail">
                                <input type="email" 
                                        className="form-control" 
                                        name="email"
                                        value={this.state.email}
                                        onChange={e => this.setState({email:e.target.value})}
                                        id="inputEmail"
                                        placeholder="Digite seu email"/>
                            </FormGroup>
                            <FormGroup label="Senha: *" htmlFor="inputSenha">
                                <input type="password" 
                                        className="form-control" 
                                        name="senha"
                                        value={this.state.senha}
                                        onChange={e => this.setState({senha:e.target.value})}
                                        id="inputSenha"
                                        placeholder="Digite sua senha"/>
                            </FormGroup>
                            <FormGroup label="Repita a senha: *" htmlFor="inputSenhaRepeticao">
                                <input type="password" 
                                        className="form-control" 
                                        name="senhaRepeticao"
                                        value={this.state.senhaRepeticao}
                                        onChange={e => this.setState({senhaRepeticao:e.target.value})}
                                        id="inputSenhaRepeticao"
                                        placeholder="Repita sua senha"/>
                            </FormGroup>
                            <button onClick = {this.cadastrar} type="button" 
                                    className="btn btn-success">
                                <i className="pi pi-save"></i>Salvar
                            </button>
                            <button onClick={this.cancelar} type="button" 
                                    className="btn btn-danger">
                                <i className="pi pi-times"></i>Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            </Card>
        )
    }
}

export default withRouter(CadastroUsuario)