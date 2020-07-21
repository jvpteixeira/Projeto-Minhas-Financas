import React from 'react'

import Card from '../components/card.js'
import FormGroup from '../components/form-group'

class CadastroUsuario extends React.Component{

    state = {
        nome : '',
        email : '',
        senha : '',
        senhaRepeticao : ''
    }

    cadastrar = () => {
        console.log(this.state)
    }
    render(){
        return (
            <div className="container">
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
                                <button onClick = {this.cadastrar} type="button" className="btn btn-success">Salvar</button>
                                <button type="button" className="btn btn-danger">Cancelar</button>
                            </div>
                        </div>
                    </div>
                </Card>
            </div>
        )
    }
}

export default CadastroUsuario