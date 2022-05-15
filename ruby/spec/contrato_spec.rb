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
      def initialize
        @edad = 10
      end
      pre { pre_edad_executed = true }
      def edad
        @edad
      end

      pre { pre_another_method_executed = true }
      def another_method
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
  end

  describe 'error cases' do
    Case = Class.new do
      pre { 1 > 3}
      def pre_fails
      end

      post { 0 == 1}
      def post_fails
      end
    end

    it 'should raise exception when executes pre method' do
      expect { Case.new.pre_fails }.to raise_error('Validation Error')
    end

    it 'should raise exception when executes post method' do
      expect { Case.new.post_fails }.to raise_error('Validation Error')
    end
  end

end