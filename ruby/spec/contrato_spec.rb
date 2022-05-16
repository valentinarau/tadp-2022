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

  describe '#pre_and_post_blocks' do
    pre_executed = false
    post_executed = false

    GenericClass = Class.new do
      attr_accessor :int_value
      def initialize
        @int_value = 0
      end

      pre { pre_executed = true }
      post { post_executed = true }
      def method_both_blocks
      end

      pre { pre_executed = true }
      def method_only_pre_block
      end

      post { post_executed = true }
      def method_only_post_block
      end

      def method_none_blocks
      end

      pre { 0 > 1 }
      def method_pre_failing_condition
      end

      pre { 1 > 0 }
      def method_pre_passing_condition
        @int_value = 2
      end

      post { 0 > 1 }
      def method_post_failing_condition
      end

      post { 1 > 0 }
      def method_post_passing_condition
        @int_value = 2
      end

      pre { 0 > 1 }
      post { post_executed = true }
      def method_pre_fail_post_pass
      end

      pre { pre_executed = true }
      post { 0 > 1 }
      def method_pre_pass_post_fail
      end

      pre { 0 > 1 }
      post { 0 > 1 }
      def method_pre_fail_post_fail
      end

      pre { pre_executed = true }
      post { post_executed = true }
      def method_pre_pass_post_pass
        @int_value = 2
      end
    end

    let(:generic_class) { GenericClass.new }
    before(:each) do
      pre_executed = false
      post_executed = false
    end

    it 'should pre and post blocks be executed for method_both_blocks' do
      generic_class.method_both_blocks
      expect(pre_executed).to be true
      expect(pre_executed).to be true
    end

    it 'should pre block only be executed for method_only_pre_block' do
      generic_class.method_only_pre_block
      expect(pre_executed).to be true
      expect(post_executed).to be false
    end

    it 'should post block only be executed for for method_only_post_block' do
      generic_class.method_only_post_block
      expect(pre_executed).to be false
      expect(post_executed).to be true
    end

    it 'should no block be executed for method_none_blocks' do
      generic_class.method_none_blocks
      expect(pre_executed).to be false
      expect(post_executed).to be false
    end

    it 'should raise a validation exception when PRE condition fails' do
      expect { generic_class.method_pre_failing_condition }.to raise_error(PreBlockValidationError)
    end

    it 'should not raise a validation execption and execute method when PRE condtion pass' do
      expect {
        generic_class.method_pre_passing_condition
        int_value = generic_class.int_value
        expect(int_value).to eq 2
      }.not_to raise_error
    end

    it 'should raise a validation execption when POST condtion fails' do
      expect { generic_class.method_post_failing_condition }.to raise_error(PostBlockValidationError)
    end

    it 'should execute method and then should not raise a validation execption when POST condtion pass' do
      expect {
        generic_class.method_post_passing_condition
        int_value = generic_class.int_value
        expect(int_value).to eq 2
      }.not_to raise_error
    end

    it 'should raise a validation execption when PRE condtion fail and POST pass' do
      expect { generic_class.method_pre_fail_post_pass }.to raise_error(PreBlockValidationError)
    end

    it 'should raise a validation execption when PRE condtion pass and POST fail' do
      expect { generic_class.method_pre_pass_post_fail }.to raise_error(PostBlockValidationError)
    end

    it 'should raise a validation execption when PRE condtion fail and POST fail' do
      expect { generic_class.method_pre_fail_post_fail }.to raise_error(PreBlockValidationError)
      # expect { generic_class.method_pre_fail_post_fail }.to raise_error()
    end

    it 'should execute PRE and POST blocks when PRE condtion pass and POST pass' do
      generic_class.method_pre_pass_post_pass
      int_value = generic_class.int_value
      expect(pre_executed).to be true
      expect(post_executed).to be true
      expect(int_value).to eq 2
    end
  end

  describe '#invariants' do
    NoInvariantsClass = Class.new do
      def generic_method
      end
    end

    SingleInvariantErrorClass = Class.new do
      @bool_var = false

      invariant { @bool_var }

      def generic_method
      end
    end

    SingleInvariantClass = Class.new do
      @bool_var = true

      invariant { @bool_var }

      def generic_method
      end
    end

    MultipleInvariantsClass = Class.new do
      @numeric_var = 10
      @str_var = 'Hi!'
      @im_an_array = []

      invariant { @numeric_var = 10 }
      invariant { @im_an_array.empty? }
      invariant { !@str_var.nil? }

      def generic_method
      end
    end

    let(:no_invariants_class) { NoInvariantsClass.new }
    let(:single_invariant_class) { SingleInvariantClass.new }
    let(:single_invariant_error_class) { SingleInvariantErrorClass.new }
    let(:multiple_invariants_class) { MultipleInvariantsClass.new }

    it 'should not raise an error for no invariants class' do
      expect { no_invariants_class.generic_method }.not_to raise_error
    end

    it 'should raise an error or print console error for single invariant with error class' do
      expect { single_invariant_error_class.generic_method }
        .to raise_error(InvariantError)
    end

    it 'should not raise an error for single (correct) invariant class' do
      expect { single_invariant_class.generic_method }.not_to raise_error
    end

    it 'should not raise an error for multiple (correct) invariant class' do
      expect { multiple_invariants_class.generic_method }.not_to raise_error
    end
  end
end

