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



      before_and_after_each_call( proc { self.set_edad 18 }, proc {  self.set_salario 1000  } )

      pre { pre_edad_executed = true }
      def edad
        puts 'la edad es ' + @edad.to_s
        @edad
      end

      def set_edad nuevaEdad
        @edad = nuevaEdad
      end

      def salario
        puts 'salario es ' + @salario.to_s
        @salario
      end

      def set_salario nuevoSalario
        @salario = nuevoSalario
      end

      pre { pre_another_method_executed = true }
      def another_method
        puts "ejemplo"
      end
    end

    it 'should execute only edad pre-block' do
      persona.edad
      expect(pre_edad_executed).to be true
      expect(pre_another_method_executed).to be false
    end

    it 'should execute edad and another_method pre-block' do
      persona.edad
      persona.another_method
      expect(pre_edad_executed).to be true
      expect(pre_another_method_executed).to be true
    end

    it 'should execute for all methods the before and after each call procs' do
      persona.edad
      persona.salario
      expect(persona.instance_variable_get(:@edad)).to eq 18
      expect(persona.instance_variable_get(:@salario)).to eq 1000
    end

  end

end