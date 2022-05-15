require_relative '../lib/contrato'

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
      pre { pre_edad_executed = true }
      def edad
        puts 'la edad es ' + @edad.to_s
        @edad
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
  end

end