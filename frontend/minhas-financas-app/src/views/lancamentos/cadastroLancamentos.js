import React from 'react'
import {withRouter} from 'react-router-dom'
import Card from '../../components/card'
import FormGroup from '../../components/form-group'
import SelectMenu from '../../components/selectMenu'
import LancamentoService from '../../services/lancamentosService'
import * as messages from '../../components/toastr'
import LocalStorageService from '../../services/localStorageService'
class CadastroLancamentos extends React.Component {

    state = {
        id: null,
        descricao: '',
        valor : '',
        mes: '',
        ano: '',
        tipo:'',
        status: '',
        usuario: null,
        atualizando:false
    }
    constructor(){
        super();
        this.service = new LancamentoService();
    }

    componentDidMount(){
        const params = this.props.match.params        
        if(params.id){
            this.service
                .obterPorId(params.id)
                .then(response => {
                    this.setState({...response.data, atualizando:true})
                }).catch(erro => {
                    messages.mensagemErro(erro.response.data)
                })
        } 
    }

    handleChange = e => {
        const value = e.target.value;
        const name = e.target.name;
        this.setState({[name]:value})
    }
    submit = () => {
        const usuarioLogado = LocalStorageService.obterItem('_usuario_logado')
        const {descricao,valor,mes,ano,tipo} = this.state;
        const lancamento = {descricao,valor,mes,ano,tipo,usuario: usuarioLogado.id}
        
        try{
            this.service.validar(lancamento)
        } catch(erro){
            const mensagens = erro.mensagens;
            mensagens.forEach(msg => messages.mensagemErro(msg))
            return false;
        }
        console.log(lancamento)
        this.service
            .salvar(lancamento)
            .then(response => {
                this.props.history.push('/consulta-lancamentos')
                messages.mensagemSucesso('Lançamento cadastrado com sucesso!')
            }).catch(error => {
                messages.mensagemErro(error.response.data)
            })
    }

    atualizar = () => {
        const {descricao,valor,mes,ano,tipo,id,usuario,status} = this.state;
        const lancamento = {descricao,valor,mes,ano,tipo,id,usuario,status}
        this.service
            .atualizar(lancamento)
            .then(response => {
                this.props.history.push('/consulta-lancamentos')
                messages.mensagemSucesso('Lançamento atualizado com sucesso!')
            }).catch(error => {
                messages.mensagemErro(error.response.data)
            })
    }

    render(){
        const tipos = this.service.obterListaTipos()
        const meses = this.service.obterListaMeses()
        return(
            <Card title={this.state.atualizando ? "Atualização de Lançamento" :"Cadastro de Lançamento" }>
                <div className="row">
                    <div className="col-md-12">
                        <FormGroup id="inputDescricao" label="Descricao: *" htmlFor="inputDescricao">
                            <input id="inputDescricao" 
                                type="text" 
                                className="form-control" 
                                name="descricao"
                                value={this.state.descricao}
                                onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-6">
                        <FormGroup id="inputAno" label="Ano: *" htmlFor="inputAno">
                            <input id="inputAno" 
                                type="text" 
                                className="form-control"
                                name="ano"
                                value={this.state.ano}
                                onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="col-md-6">
                        <FormGroup id="inputMes" label="Mes: *"  htmlFor="inputMes">
                            <SelectMenu id="inputMes" lista={meses} 
                                className="form-control"
                                name="mes"
                                value={this.state.mes}
                                onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-4">
                        <FormGroup id="inputValor" label="Valor: *" htmlFor="inputValor">
                            <input id="inputValor" 
                                type="text" 
                                className="form-control"
                                name="valor"
                                value={this.state.valor}
                                onChange={this.handleChange}/>
                        </FormGroup>
                    </div>
                    <div className="col-md-4">
                        <FormGroup id="inputTipo" label="Tipo: *" htmlFor="inputTipo">
                            <SelectMenu 
                                id="inputTipo" 
                                lista={tipos} 
                                className="form-control"
                                name="tipo"
                                value={this.state.tipo}
                                onChange={this.handleChange} />
                        </FormGroup>
                    </div>
                    <div className="col-md-4">
                        <FormGroup id="inputStatus" label="Status: *">
                            <input type="text" 
                                className="form-control" 
                                name="status"
                                value={this.state.status}
                                disabled />
                        </FormGroup>
                    </div>
                </div>

                <div className="row">
                    <div className="col-md-6">
                        {this.state.atualizando ? 
                            (
                                <button className="btn btn-primary" 
                                    onClick = {this.atualizar}>
                                        <i className="pi pi-refresh" ></i>Atualizar
                                    </button>
                            ) 
                            :
                            (
                                <button className="btn btn-success" 
                                    onClick = {this.submit}>
                                       <i className="pi pi-save"></i> Salvar
                                    </button>   
                            )
                        }
                        <button onClick={e => this.props.history.push('/consulta-lancamentos')} 
                            className="btn btn-danger">
                                <i className="pi pi-times"></i> Cancelar
                                </button>
                    </div>
                </div>
            </Card>
        )
    }
}

export default withRouter(CadastroLancamentos)