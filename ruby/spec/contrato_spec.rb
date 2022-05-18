require_relative '../lib/contratos_framework'

describe Contrato do

  describe 'blocks PRE' do
    pre_edad_executed = false
    pre_another_method_executed = false
    let(:persona) { Persona.new }

    before(:each) do
      pre_edad_executed = false
      pre_another_method_executed = false
    end

    Persona = Class.new do

      before_and_after_each_call( proc { self.set_edad 10 }, proc {  self.set_salario 1000  } )

      def initialize
        @edad = 9
      end
      pre { pre_edad_executed = true }
      def edad
        puts 'la edad es ' + @edad.to_s
        @edad
      end

      def salario
        puts 'salario es ' + @salario.to_s
        @salario
      end

      def set_salario nuevoSalario
        @salario = nuevoSalario
      end

      def set_edad nuevaEdad
        @edad = nuevaEdad
      end

      pre { pre_another_method_executed = true }
      def another_method
        puts "ejemplo"
      end
    end

    it 'should execute only edad pre-block' do
      edad = persona.edad
      expect(edad).to be 10
      expect(pre_edad_executed).to be true
      expect(pre_another_method_executed).to be false
    end

    it 'should execute edad and another_method pre-block' do
      edad = persona.edad
      persona.another_method
      expect(edad).to be 10
      expect(pre_edad_executed).to be true
      expect(pre_another_method_executed).to be true
    end

    it 'should execute for all methods the before and after each call procs' do
      expect(persona.instance_variable_get(:@edad)).to eq 9
      expect(persona.instance_variable_get(:@salario)).to be nil
      edad = persona.edad
      salario = persona.salario
      expect(edad).to eq 10
      expect(salario).to eq 1000
    end

    it 'should evaluate method before execute global after block' do
      salario = persona.salario
      expect(salario).to be nil
    end

  end

  describe 'error cases' do
    ErrorCase = Class.new do
      pre { 1 > 3}
      def pre_fails
      end

      post { 0 == 1}
      def post_fails
      end
    end

    it 'should raise exception when executes pre method' do
      expect { ErrorCase.new.pre_fails }.to raise_error('Validation Error')
    end

    it 'should raise exception when executes post method' do
      expect { ErrorCase.new.post_fails }.to raise_error('Validation Error')
    end
  end

  describe 'blocks execution context' do
    let(:instance) {CaseWithContext.new}

    CaseWithContext = Class.new do
      pre { set_algo true }
      def get_algo
        @algo
      end
      def set_algo val
        @algo = val
      end
    end

    it 'should execute in instance context' do
      expect(instance.get_algo).to be true
    end
  end

end