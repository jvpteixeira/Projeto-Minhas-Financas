import React from 'react'
import Card from '../components/card'
import FormGroup from '../components/form-group'
import {withRouter} from 'react-router-dom'
import {mensagemErro} from '../components/toastr'
import UsuarioService from '../services/usuarioService.js'
import LocalStorageService from '../services/localStorageService'

class Login extends React.Component{
  
  state = {
      email : '',
      senha: '',
      mensagemErro: null
  }
  
  constructor(){
    super();
    this.service = new UsuarioService();
  }

  entrar = () => {
    this.service.autenticar({
        email:this.state.email,
        senha:this.state.senha
    }).then(response => {
        LocalStorageService.addItem('_usuario_logado',response.data)
        this.props.history.push('/home')
    }).catch(error => {
        mensagemErro(error.response.data);
    })
  }

  prepareCadastrar = () => {
    this.props.history.push('/cadastro-usuario')
  }

  render(){
    return (
      <div className="row">  
        <div className="col-md-6" style={{position : 'relative', left: '300px'}}>
          <div className="bs-docs-section">
            <Card title="Login">
                <div className="row">
                  <div className="col-lg-12">
                    <div className="bs-component">
                      <div className="form-group">
                        <FormGroup label="Email: *" htmlFor="exampleInputEmail1">
                        <input type="email" 
                                className="form-control" 
                                value={this.state.email}
                                onChange={e => this.setState({email:e.target.value})}
                                id="exampleInputEmail1" 
                                aria-describedby="emailHelp" 
                                placeholder="Digite o Email"/>
                        </FormGroup>
                        
                        <FormGroup label="Senha: *" htmlFor="exampleInputPassword1">
                        <input type="password" 
                                className="form-control" 
                                value={this.state.senha}
                                onChange={e => this.setState({senha:e.target.value})}
                                id="exampleInputPassword1" 
                                placeholder="Password"/>
                        </FormGroup>

                        <button onClick={this.entrar} type="button" className="btn btn-success">
                          <i className="pi pi-sign-in"></i>  Entrar
                        </button>
                        <button onClick={this.prepareCadastrar} type="button" className="btn btn-danger" href="#/cadastro-usuario">
                          <i className="pi pi-plus"></i>  Cadastrar
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
            </Card>
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(Login)
